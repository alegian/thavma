package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.client.util.scale
import me.alegian.thavma.impl.common.block.entity.PedestalBE
import me.alegian.thavma.impl.common.block.entity.itemHandler
import me.alegian.thavma.impl.common.data.capability.firstStack
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.ItemDisplayContext
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

// item rotation speed
private const val DEGREES_PER_TICK = 5

class PedestalBER : GeoBlockRenderer<PedestalBE>(DefaultedBlockGeoModel(rl("infusion_pedestal"))) {
  init {
    addRenderLayer(EmissiveGeoLayer<PedestalBE>(this))
  }

  override fun actuallyRender(
    poseStack: PoseStack, be: PedestalBE, model: BakedGeoModel, renderType: RenderType?,
    bufferSource: MultiBufferSource, buffer: VertexConsumer?, isReRender: Boolean, partialTick: Float, packedLight: Int,
    packedOverlay: Int, colour: Int
  ) {
    super.actuallyRender(poseStack, be, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour)
    val stack = be.itemHandler?.firstStack ?: return
    if(stack.isEmpty) return

    val itemRenderer = Minecraft.getInstance().itemRenderer
    val level = be.level ?: return

    val randSeed = be.blockPos.asLong().toInt()

    poseStack.use {
      translate(0.0, 1.0, 0.0)
      scale(0.8f)
      mulPose(Axis.YP.rotationDegrees(((level.gameTime + partialTick) * DEGREES_PER_TICK) % 360))
      itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, level, randSeed)
    }
  }
}
