package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.util.rotateX
import me.alegian.thavma.impl.client.util.rotateZ
import me.alegian.thavma.impl.common.block.entity.EssentiaContainerBE
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.InventoryMenu
import net.neoforged.neoforge.client.RenderTypeHelper

class EssentiaContainerBER: BlockEntityRenderer<EssentiaContainerBE> {
  override fun render(blockEntity: EssentiaContainerBE, partialTick: Float, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int, packedOverlay: Int) {
    poseStack.pushPose()

    val height = 10/16f
    poseStack.translate(0.5, 1/16.0 + height/2, 0.5)
    poseStack.scale(8/16f, height, 8/16f)

    val renderType = RenderTypeHelper.getEntityRenderType(RenderType.cutout(), false)
    renderUnitCube(poseStack, bufferSource.getBuffer(renderType), packedLight)

    poseStack.popPose()
  }
}

private fun renderUnitCube(poseStack: PoseStack, buffer: VertexConsumer, packedLight: Int) {
  poseStack.use{
    //top
    renderUnitQuad(poseStack.last(), buffer, packedLight)
    //sides
    rotateZ(90)
    renderUnitQuad(poseStack.last(), buffer, packedLight)
    rotateX(90)
    renderUnitQuad(poseStack.last(), buffer, packedLight)
    rotateX(90)
    renderUnitQuad(poseStack.last(), buffer, packedLight)
    rotateX(90)
    renderUnitQuad(poseStack.last(), buffer, packedLight)
    //bot
    rotateX(90)
    rotateZ(90)
    renderUnitQuad(poseStack.last(), buffer, packedLight)
  }
}

private fun renderUnitQuad(pose: PoseStack.Pose, buffer: VertexConsumer, packedLight: Int) {
  val color = Aspects.IGNIS.get().color

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
