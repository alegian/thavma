package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.init.registries.deferred.T7Items.THAVMITE_KATANA
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult


object EntityHelper {
  fun isEntityWearingBoots(entity: LivingEntity): Boolean {
    val boots = entity.getItemBySlot(EquipmentSlot.FEET)
    return boots.`is`(ItemTags.FOOT_ARMOR)
  }

  fun invertSwingingArm(pLivingEntity: LivingEntity) {
    if (pLivingEntity.swingingArm == InteractionHand.MAIN_HAND)
      pLivingEntity.swingingArm = InteractionHand.OFF_HAND
    else pLivingEntity.swingingArm = InteractionHand.MAIN_HAND
  }

  fun isHandKatana(hand: InteractionHand): Boolean {
    return Minecraft.getInstance().player?.getItemInHand(hand)?.item == THAVMITE_KATANA.get()
  }

  fun getServerHitResult(player: ServerPlayer): BlockHitResult {
    return player.level().clip(
      ClipContext(
        player.getEyePosition(1f),
        player.getEyePosition(1f).add(
          player.getViewVector(1f).scale(6.0)
        ),
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        player
      )
    )
  }
}
