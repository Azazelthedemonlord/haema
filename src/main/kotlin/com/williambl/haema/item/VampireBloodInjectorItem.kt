package com.williambl.haema.item

import com.williambl.haema.Vampirable
import com.williambl.haema.VampireBloodManager
import com.williambl.haema.damagesource.IncompatibleBloodDamageSource
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class VampireBloodInjectorItem(settings: Settings?) : Item(settings) {

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val emptyStack = ItemStack(Registry.ITEM.get(Identifier("haema:empty_vampire_blood_injector")))
        if ((user as Vampirable).isVampire) {
            (user.hungerManager as VampireBloodManager).addBlood(6.0)
            return TypedActionResult.consume(emptyStack)
        }

        if (!user.hasStatusEffect(StatusEffects.STRENGTH) || user.getStatusEffect(StatusEffects.STRENGTH)!!.amplifier <= 0) {
            user.addStatusEffect(StatusEffectInstance(StatusEffects.WITHER, 3000))
            user.addStatusEffect(StatusEffectInstance(StatusEffects.NAUSEA, 100))
            user.damage(IncompatibleBloodDamageSource.instance, 20f)
            return TypedActionResult.consume(emptyStack)
        }

        Vampirable.convert(user)
        return TypedActionResult.consume(emptyStack)
    }
}