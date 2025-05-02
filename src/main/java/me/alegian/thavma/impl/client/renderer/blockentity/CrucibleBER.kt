package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.common.block.entity.CrucibleBE
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.client.RenderTypeHelper
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions

class CrucibleBER : BlockEntityRenderer<CrucibleBE> {
  override fun render(crucibleBE: CrucibleBE, pPartialTick: Float, pPoseStack: PoseStack, pBufferSource: MultiBufferSource, pPackedLight: Int, pPackedOverlay: Int) {
    val tank = crucibleBE.fluidHandler

    if (tank.isEmpty) return

    pPoseStack.pushPose()

    pPoseStack.translate(0.5, crucibleBE.waterHeight, 0.5)

    val renderType = RenderTypeHelper.getEntityRenderType(RenderType.translucent(), false)
    renderWaterQuad(pPoseStack.last(), pBufferSource.getBuffer(renderType), pPackedLight)

    pPoseStack.popPose()
  }
}

private fun renderWaterQuad(pose: PoseStack.Pose, buffer: VertexConsumer, packedLight: Int) {
  val waterClientExtensions = IClientFluidTypeExtensions.of(Fluids.WATER)

  val sprite = getFluidSprite(waterClientExtensions)
  val color = waterClientExtensions.tintColor

  val width = 12 / 16f

  val minU = sprite.u0
  val maxU = sprite.u1
  val minV = sprite.v0
  val maxV = sprite.v1

  buffer.addVertex(pose, -width / 2, 0f, -width / 2)
    .setColor(color)
    .setUv(minU, minV)
    .setLight(packedLight)
    .setOverlay(OverlayTexture.NO_OVERLAY)
    .setNormal(pose, 0f, 1f, 0f)

  buffer.addVertex(pose, -width / 2, 0f, width / 2)
    .setColor(color)
    .setUv(minU, maxV)
    .setLight(packedLight)
    .setOverlay(OverlayTexture.NO_OVERLAY)
    .setNormal(pose, 0f, 1f, 0f)

  buffer.addVertex(pose, width / 2, 0f, width / 2)
    .setColor(color)
    .setUv(maxU, maxV)
    .setLight(packedLight)
    .setOverlay(OverlayTexture.NO_OVERLAY)
    .setNormal(pose, 0f, 1f, 0f)

  buffer.addVertex(pose, width / 2, 0f, -width / 2)
    .setColor(color)
    .setUv(maxU, minV)
    .setLight(packedLight)
    .setOverlay(OverlayTexture.NO_OVERLAY)
    .setNormal(pose, 0f, 1f, 0f)
}

private fun getFluidSprite(fluidTypeExtensions: IClientFluidTypeExtensions): TextureAtlasSprite {
  return Minecraft.getInstance()
    .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
    .apply(fluidTypeExtensions.stillTexture)
}
