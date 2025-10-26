package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.util.rotateX
import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.common.block.entity.SealingJarBE
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.data.capability.IAspectContainer
import me.alegian.thavma.impl.common.util.use
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu
import net.neoforged.neoforge.client.RenderTypeHelper

private const val MAX_HEIGHT = 10 / 16f

class SealingJarBER : BlockEntityRenderer<SealingJarBE> {
  override fun render(be: SealingJarBE, partialTick: Float, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    AspectContainer.at(be.level, be.blockPos)?.let {
      renderContents(it, poseStack, bufferSource, packedLight)
    }
  }

  companion object {
    fun renderContents(aspectContainer: IAspectContainer, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int) {
      val stack = aspectContainer.aspects.firstOrNull() ?: return
      val color = stack.aspect.color

      poseStack.use {
        val height = MAX_HEIGHT * stack.amount / aspectContainer.capacity.toFloat()
        translate(0.5, 1 / 16.0 + height / 2, 0.5)
        scale(8 / 16f, height, 8 / 16f)
        val renderType = RenderTypeHelper.getEntityRenderType(RenderType.cutout(), false)
        renderUnitCube(this, bufferSource.getBuffer(renderType), packedLight, color)
      }
    }

    private fun renderUnitCube(poseStack: PoseStack, buffer: VertexConsumer, packedLight: Int, color: Int) {
      poseStack.use {
        //top
        renderUnitQuad(poseStack.last(), buffer, packedLight, color)
        //sides
        rotateZ(90)
        renderUnitQuad(poseStack.last(), buffer, packedLight, color)
        rotateX(90)
        renderUnitQuad(poseStack.last(), buffer, packedLight, color)
        rotateX(90)
        renderUnitQuad(poseStack.last(), buffer, packedLight, color)
        rotateX(90)
        renderUnitQuad(poseStack.last(), buffer, packedLight, color)
        //bot
        rotateX(90)
        rotateZ(90)
        renderUnitQuad(poseStack.last(), buffer, packedLight, color)
      }
    }

    private fun renderUnitQuad(pose: PoseStack.Pose, buffer: VertexConsumer, packedLight: Int, color: Int) {
      val sprite = Minecraft.getInstance()
        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
        .apply(ResourceLocation.fromNamespaceAndPath("neoforge", "white"))

      val minU = sprite.u0
      val maxU = sprite.u1
      val minV = sprite.v0
      val maxV = sprite.v1

      buffer.addVertex(pose, -0.5f, 0.5f, -0.5f)
        .setColor(color)
        .setUv(minU, minV)
        .setLight(packedLight)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setNormal(pose, 0f, 1f, 0f)

      buffer.addVertex(pose, -0.5f, 0.5f, 0.5f)
        .setColor(color)
        .setUv(minU, maxV)
        .setLight(packedLight)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setNormal(pose, 0f, 1f, 0f)

      buffer.addVertex(pose, 0.5f, 0.5f, 0.5f)
        .setColor(color)
        .setUv(maxU, maxV)
        .setLight(packedLight)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setNormal(pose, 0f, 1f, 0f)

      buffer.addVertex(pose, 0.5f, 0.5f, -0.5f)
        .setColor(color)
        .setUv(maxU, minV)
        .setLight(packedLight)
        .setOverlay(OverlayTexture.NO_OVERLAY)
        .setNormal(pose, 0f, 1f, 0f)
    }
  }
}

