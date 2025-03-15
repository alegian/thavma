package me.alegian.thavma.impl.client.renderer.geo.layer;

import me.alegian.thavma.impl.client.T7RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class EmissiveGeoLayer<T extends GeoAnimatable> extends AutoGlowingGeoLayer<T> {
  public EmissiveGeoLayer(GeoRenderer<T> renderer) {
    super(renderer);
  }

  @Override
  protected RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
    return T7RenderTypes.INSTANCE.getEYES_WITH_DEPTH().apply(AutoGlowingTexture.getEmissiveResource(this.getTextureResource(animatable)));
  }
}
