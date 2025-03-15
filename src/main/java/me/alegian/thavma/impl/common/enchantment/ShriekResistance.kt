package me.alegian.thavma.impl.common.enchantment

import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.enchantment.EnchantmentHelper
import org.apache.commons.lang3.mutable.MutableFloat

object ShriekResistance {
    val LOCATION: ResourceLocation = rl("shriek_resistance")

    /**
     * Custom version of EnchantmentHelper.getDamageProtection
     * Divides by 4 to account for 4 pieces of armor.
     * Does not account for other enchants like Protection, these should have no effect.
     */
    fun getDamageProtection(level: ServerLevel, entity: LivingEntity, damageSource: DamageSource): Float {
        val mutableFloat = MutableFloat(0.0f)
        EnchantmentHelper.runIterationOnEquipment(
            entity
        ) { enchantmentHolder, enchantmentLevel, enchantedItemInUse ->
            if (enchantmentHolder.`is`(LOCATION)) enchantmentHolder.value()
                .modifyDamageProtection(
                    level,
                    enchantmentLevel,
                    enchantedItemInUse.itemStack(),
                    entity,
                    damageSource,
                    mutableFloat
                )
        }
        return mutableFloat.toFloat() / 4f
    }
}
