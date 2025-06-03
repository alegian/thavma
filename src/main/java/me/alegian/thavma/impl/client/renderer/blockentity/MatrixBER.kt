package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.client.renderer.level.renderEssentia
import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import me.alegian.thavma.impl.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class MatrixBER : GeoBlockRenderer<MatrixBE>(DefaultedBlockGeoModel(rl("infusion_matrix"))) {
  init {
    addRenderLayer(EmissiveGeoLayer<MatrixBE>(this))
  }

  override fun renderCubesOfBone(poseStack: PoseStack, bone: GeoBone, buffer: VertexConsumer, packedLight: Int, packedOverlay: Int, colour: Int) {
    if (!animatable.hasRing && bone.parent?.parent?.name == "bone") return

    return super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, colour)
  }

  override fun actuallyRender(poseStack: PoseStack, be: MatrixBE, model: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, isReRender: Boolean, partialTick: Float, packedLight: Int, packedOverlay: Int, colour: Int) {
    super.actuallyRender(poseStack, be, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour)
    if (be.flyingAspects.isEmpty()) return

    val flyingStream = be.flyingAspects.filterNotNull().associate {
      Pair(
        it.blockPos.center,
        trajectoryLength(it.blockPos.center, be.blockPos.center)
      )
    }
    val ticks = Minecraft.getInstance().levelRenderer.ticks + partialTick
    for (f in flyingStream) {
      val firstIndex = be.flyingAspects.indexOfFirst { f.key == it?.blockPos?.center }
      val head = f.value - 1 - firstIndex
      val length = 1 + firstIndex - be.flyingAspects.indexOfLast { f.key == it?.blockPos?.center }
      renderEssentia(f.key, be.blockPos.center, head, length, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), ticks, Aspects.PRAECANTATIO.get().color)
    }
  }
}
