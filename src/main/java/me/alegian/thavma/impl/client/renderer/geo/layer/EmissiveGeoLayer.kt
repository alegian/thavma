package me.alegian.thavma.impl.client.renderer.geo.layer

import me.alegian.thavma.impl.client.T7RenderTypes.EYES_WITH_DEPTH
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.cache.texture.AutoGlowingTexture
import software.bernie.geckolib.renderer.GeoRenderer
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer

class EmissiveGeoLayer<T : GeoAnimatable>(renderer: GeoRenderer<T>) : AutoGlowingGeoLayer<T>(renderer) {
  override fun getRenderType(animatable: T, bufferSource: MultiBufferSource?): RenderType {
    return EYES_WITH_DEPTH.apply(AutoGlowingTexture.getEmissiveResource(getTextureResource(animatable)))
  }
}
