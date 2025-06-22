package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import me.alegian.thavma.impl.client.util.scale
import me.alegian.thavma.impl.common.block.entity.AuraNodeBE
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.SEALING_JAR
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.util.Mth
import net.neoforged.neoforge.client.model.data.ModelData
import kotlin.math.max

class AuraNodeBER : BlockEntityRenderer<AuraNodeBE> {
  private var scale = 1f

  override fun render(be: AuraNodeBE, partialTick: Float, poseStack: PoseStack, bufferSource: MultiBufferSource, combinedLight: Int, combinedOverlay: Int) {
    val containingCountdown = be.containingCountdown

    poseStack.pushPose()
    setupPose(poseStack, containingCountdown, partialTick)

    renderContainer(poseStack, bufferSource, combinedLight, combinedOverlay, containingCountdown)
    renderNode(be, poseStack, bufferSource)

    poseStack.popPose()
  }

  /**
   * Translates to the center of the block.
   * Smoothly scales down according to containment progress
   */
  private fun setupPose(poseStack: PoseStack, containingCountdown: Int, partialTick: Float) {
    poseStack.translate(0.5, 0.5, 0.5)
    scale = 1f

    if (containingCountdown < 0) return

    scale = max(
      Mth.lerp(
        partialTick,
        containingCountdown.toFloat() / AuraNodeBE.MAX_COUNTDOWN,
        (containingCountdown - 1).toFloat() / AuraNodeBE.MAX_COUNTDOWN
      ).toDouble(), MIN_SCALE.toDouble()
    ).toFloat()

    poseStack.scale(scale)
  }

  private fun renderNode(be: AuraNodeBE, poseStack: PoseStack, bufferSource: MultiBufferSource) {
    poseStack.use {
      // follows the camera like a particle
      val rotation = Minecraft.getInstance().gameRenderer.mainCamera.rotation()
      poseStack.mulPose(rotation)

      // empty nodes look like small black circles
      AspectContainer.from(be)?.aspects?.toSortedList()?.run {
        if (this.isNotEmpty())
          for (stack in this)
            renderAuraNodeLayer(poseStack, bufferSource, stack.aspect.color, 0.4f, stack.amount / 2f / AuraNodeBE.MAX_ASPECTS)
        else renderAuraNodeLayer(poseStack, bufferSource, 0, 1f, 1f / AuraNodeBE.MAX_ASPECTS)
      }
    }
  }

  companion object {
    private const val MIN_SCALE = 1f / 3 // for lerping containment animation

    /**
     * Renders a 3x3x3 container jar around the node.
     */
    private fun renderContainer(poseStack: PoseStack, bufferSource: MultiBufferSource, combinedLight: Int, combinedOverlay: Int, containingCountdown: Int) {
      if (containingCountdown < 0) return

      poseStack.pushPose()
      poseStack.scale(3f)
      poseStack.translate(-0.5, -0.5, -0.5)
      Minecraft.getInstance().blockRenderer.renderSingleBlock(SEALING_JAR.get().defaultBlockState(), poseStack, bufferSource, combinedLight, combinedOverlay, ModelData.EMPTY, RenderType.translucent())
      poseStack.popPose()
    }
  }
}
