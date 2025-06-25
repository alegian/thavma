package me.alegian.thavma.impl.client.renderer.geo.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import me.alegian.thavma.impl.client.T7Colors
import me.alegian.thavma.impl.client.T7RenderTypes.EYES_WITH_DEPTH
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.cache.texture.AutoGlowingTexture
import software.bernie.geckolib.renderer.GeoRenderer
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer

class EmissiveGeoLayer<T : GeoAnimatable>(renderer: GeoRenderer<T>) : AutoGlowingGeoLayer<T>(renderer) {
  override fun getRenderType(animatable: T, bufferSource: MultiBufferSource?): RenderType {
    return EYES_WITH_DEPTH.apply(AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable)))
  }

  override fun render(poseStack: PoseStack, animatable: T, bakedModel: BakedGeoModel, renderType: RenderType?, bufferSource: MultiBufferSource, buffer: VertexConsumer?, partialTick: Float, packedLight: Int, packedOverlay: Int) {
    val renderType = getRenderType(animatable, bufferSource)

    getRenderer().reRender(
      bakedModel, poseStack, bufferSource, animatable, renderType,
      bufferSource.getBuffer(renderType), partialTick, LightTexture.FULL_SKY, packedOverlay,
      T7Colors.GREEN
    )
  }
}
