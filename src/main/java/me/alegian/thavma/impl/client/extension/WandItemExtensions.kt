package me.alegian.thavma.impl.client.extension

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import org.joml.Matrix4f

class WandItemExtensions : IClientItemExtensions {
  override fun applyForgeHandTransform(poseStack: PoseStack, player: LocalPlayer, arm: HumanoidArm, itemInHand: ItemStack, partialTick: Float, equipProcess: Float, swingProcess: Float): Boolean {
    val i = if (arm == HumanoidArm.RIGHT) 1 else -1
    var transformMatrix = Matrix4f()
    var using = false

    if (player.getUseItem() == itemInHand && player.isUsingItem) {
      using = true

      transformMatrix = transformMatrix
        .translate(i * 0.56f, -0.52f, -0.72f)
        .rotateX((-1 * Math.PI / 4).toFloat())
        .rotateZ((i * Math.PI / 16).toFloat())
    }

    poseStack.mulPose(transformMatrix)
    return using
  }
}
