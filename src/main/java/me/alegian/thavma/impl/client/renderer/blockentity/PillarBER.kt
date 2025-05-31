package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.common.block.entity.PillarBE
import me.alegian.thavma.impl.init.registries.T7BlockStateProperties.MASTER
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class PillarBER : GeoBlockRenderer<PillarBE>(DefaultedBlockGeoModel(rl("infusion_pillar"))) {
  init {
    addRenderLayer(EmissiveGeoLayer(this))
  }

  override fun actuallyRender(poseStack: PoseStack, be: PillarBE, model: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, isReRender: Boolean, partialTick: Float, packedLight: Int, packedOverlay: Int, colour: Int) {
    if(!be.blockState.getValue(MASTER)) return
    super.actuallyRender(poseStack, be, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour)
  }

  override fun getRenderBoundingBox(be: PillarBE) = be.renderAABB
}
