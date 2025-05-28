package me.alegian.thavma.impl.client.extension

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.common.enumextension.ArcaneLensArmPose
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class ArcaneLensItemExtensions : IClientItemExtensions {
  override fun getArmPose(entityLiving: LivingEntity, hand: InteractionHand, itemStack: ItemStack) = ArcaneLensArmPose.value()

  override fun applyForgeHandTransform(poseStack: PoseStack, player: LocalPlayer, arm: HumanoidArm, itemInHand: ItemStack, partialTick: Float, equipProcess: Float, swingProcess: Float): Boolean {
    // pretty much nullifies the default right click bob
    if (player.getUseItem() == itemInHand && player.isUsingItem) {
      val i = if (arm == HumanoidArm.RIGHT) 1 else -1
      poseStack.translate(i * 0.56f, -0.53f, -0.72f)
      return true
    }
    return false
  }
}
