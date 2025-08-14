package me.alegian.thavma.impl.client.renderer.blockentity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.client.renderer.geo.layer.ItemRenderLayer
import me.alegian.thavma.impl.client.renderer.renderFlyingAspects
import me.alegian.thavma.impl.client.util.translate
import me.alegian.thavma.impl.common.block.entity.MatrixBE
import me.alegian.thavma.impl.common.infusion.trajectoryLength
import me.alegian.thavma.impl.common.util.unaryMinus
import me.alegian.thavma.impl.common.util.use
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.INFUSION_STATE
import me.alegian.thavma.impl.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.phys.AABB
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.`object`.GeoBone
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer
import software.bernie.geckolib.renderer.layer.GeoRenderLayer

class MatrixBER : GeoBlockRenderer<MatrixBE>(DefaultedBlockGeoModel(rl("infusion_matrix"))) {
  init {
    addRenderLayer(EmissiveGeoLayer(this))
    addRenderLayer(ItemRenderLayer(this, 0.375f))
    addRenderLayer(FlyingAspectsRenderLayer(this))
  }

  override fun getRenderBoundingBox(blockEntity: MatrixBE) = AABB.INFINITE

  override fun renderRecursively(poseStack: PoseStack, animatable: MatrixBE, bone: GeoBone, renderType: RenderType, bufferSource: MultiBufferSource, buffer: VertexConsumer, isReRender: Boolean, partialTick: Float, packedLight: Int, packedOverlay: Int, colour: Int) {
    if (bone.name == "bone" && !animatable.hasRing) return
    super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour)
  }
}


private class FlyingAspectsRenderLayer(renderer: MatrixBER) : GeoRenderLayer<MatrixBE>(renderer) {
  override fun render(poseStack: PoseStack, be: MatrixBE, bakedModel: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, partialTick: Float, packedLight: Int, packedOverlay: Int) {
    val flyingAspects = be.get(INFUSION_STATE)?.flyingAspects
    if (flyingAspects?.isEmpty() ?: true) return

    poseStack.use {
      translate(-be.blockPos.center)
      val flyingStream = flyingAspects.filterNotNull().associate {
        Pair(
          it.blockPos.center,
          trajectoryLength(it.blockPos.center, be.drainPos)
        )
      }
      val ticks = Minecraft.getInstance().levelRenderer.ticks + partialTick
      for (f in flyingStream) {
        val firstIndex = flyingAspects.indexOfFirst { f.key == it?.blockPos?.center }
        val head = f.value - 1 - firstIndex
        val length = 1 + flyingAspects.indexOfLast { f.key == it?.blockPos?.center } - firstIndex
        val aspect = flyingAspects[firstIndex]?.aspectStack?.aspect ?: continue
        val colorWithAlpha = 0x44000000 or (aspect.color and 0xffffff)
        if (head < 0) continue
        renderFlyingAspects(f.key, be.drainPos, 1.2, head, length, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), ticks, colorWithAlpha, 0.12)
      }
    }
  }
}
