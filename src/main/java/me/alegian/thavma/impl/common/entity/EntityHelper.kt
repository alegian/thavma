package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.init.registries.T7Tags
import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_KATANA
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult

fun LivingEntity.isWearingStepHeightBoots(): Boolean {
  val boots = getItemBySlot(EquipmentSlot.FEET)
  return boots.`is`(T7Tags.Items.STEP_HEIGHT)
}

object EntityHelper {
  fun invertSwingingArm(pLivingEntity: LivingEntity) {
    if (pLivingEntity.swingingArm == InteractionHand.MAIN_HAND)
      pLivingEntity.swingingArm = InteractionHand.OFF_HAND
    else pLivingEntity.swingingArm = InteractionHand.MAIN_HAND
  }

  fun isHandKatana(hand: InteractionHand): Boolean {
    return Minecraft.getInstance().player?.getItemInHand(hand)?.item == THAVMITE_KATANA.get()
  }
}
