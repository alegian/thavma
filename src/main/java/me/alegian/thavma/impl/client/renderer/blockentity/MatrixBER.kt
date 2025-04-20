package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.rl
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class MatrixBER : GeoBlockRenderer<MatrixBE>(DefaultedBlockGeoModel<MatrixBE>(rl("infusion_matrix"))) {
  init {
    addRenderLayer(EmissiveGeoLayer<MatrixBE>(this))
  }

  override fun renderCubesOfBone(poseStack: PoseStack, bone: GeoBone, buffer: VertexConsumer, packedLight: Int, packedOverlay: Int, colour: Int) {
    if (!animatable.hasRing && bone.parent?.parent?.name == "bone") return

    return super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour)
  }
}
