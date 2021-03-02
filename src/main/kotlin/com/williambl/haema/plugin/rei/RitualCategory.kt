package com.williambl.haema.plugin.rei

import com.williambl.haema.craft.ritual.RitualRecipe
import com.williambl.haema.ritualTable
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeCategory
import me.shedaniel.rei.api.RecipeDisplay
import me.shedaniel.rei.api.widgets.Widgets
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.client.resource.language.I18n
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

class RitualCategory: RecipeCategory<RitualCategory.Display> {
    class Display(val recipe: RitualRecipe): RecipeDisplay {
        private val input: MutableList<List<EntryStack>> = EntryStack.ofIngredients(recipe.ingredients).toMutableList()
        init {
            input.add(mutableListOf(EntryStack.create(recipe.fluid)))
        }

        override fun getInputEntries(): List<List<EntryStack>> = input

        override fun getResultingEntries(): List<List<EntryStack>> = listOf(listOf())

        override fun getRecipeCategory(): Identifier = Identifier("haema:ritual")
    }

    override fun getIdentifier(): Identifier = Identifier("haema:ritual")

    override fun getCategoryName(): String = I18n.translate("rei.${identifier.toString().replace(':', '.')}")

    override fun setupDisplay(recipeDisplay: Display, bounds: Rectangle): MutableList<Widget> {
        val inputsPoint = Point(bounds.centerX - 64, bounds.centerY - 16)
        val outputPoint = Point(bounds.centerX + 32, bounds.centerY - 8)
        val widgets = mutableListOf<Widget>()

        val inputEntries = recipeDisplay.inputEntries
        widgets.addAll(inputEntries.mapIndexed { i, stacks ->
            Widgets.createSlot(Point(inputsPoint.x+((i+1)%2)*18, inputsPoint.y+(i/2)*18)).entries(stacks)
        })

        widgets.add(Widgets.createLabel(Point(bounds.centerX, bounds.centerY-32),
            TranslatableText("gui.haema.altar_level", recipeDisplay.recipe.minLevel).formatted(Formatting.UNDERLINE))
        )

        widgets.add(Widgets.createLabel(Point(bounds.centerX+16, bounds.centerY+16), TranslatableText("gui.haema.repeatable.${recipeDisplay.recipe.isRepeatable}").formatted(if (recipeDisplay.recipe.isRepeatable) Formatting.GREEN else Formatting.RED)))

        widgets.add(Widgets.createLabel(outputPoint, TranslatableText("ritual.action.${recipeDisplay.recipe.actionName}", recipeDisplay.recipe.actionArg)))

        return widgets
    }

    override fun getLogo(): EntryStack = EntryStack.create(ritualTable.asItem().defaultStack)
}