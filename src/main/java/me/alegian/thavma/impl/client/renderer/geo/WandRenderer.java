package me.alegian.thavma.impl.client.renderer.geo;

import me.alegian.thavma.impl.common.item.WandItem;
import me.alegian.thavma.impl.common.wand.WandCoreMaterial;
import me.alegian.thavma.impl.common.wand.WandHandleMaterial;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;

import javax.annotation.Nullable;

import static me.alegian.thavma.impl.ThavmaKt.rl;

public class WandRenderer extends DynamicGeoItemRenderer<WandItem> {
  private final ResourceLocation handleLocation;
  private final ResourceLocation coreLocation;

  public WandRenderer(WandHandleMaterial handleMaterial, WandCoreMaterial coreMaterial) {
    super(new DefaultedItemGeoModel<>(rl("wand")));
    this.handleLocation = WandRenderer.handleTexture(handleMaterial.getRegisteredLocation());
    this.coreLocation = WandRenderer.coreTexture(coreMaterial.getRegisteredLocation());
  }

  private static ResourceLocation handleTexture(ResourceLocation registeredLocation) {
    return WandRenderer.texture(registeredLocation, "wand_handle_");
  }

  private static ResourceLocation coreTexture(ResourceLocation registeredLocation) {
    return WandRenderer.texture(registeredLocation, "wand_core_");
  }

  private static ResourceLocation texture(ResourceLocation registeredLocation, String prefix) {
    return registeredLocation.withPrefix("textures/item/" + prefix).withSuffix(".png");
  }

  @Nullable
  @Override
  protected ResourceLocation getTextureOverrideForBone(GeoBone bone, WandItem animatable, float partialTick) {
    return switch (bone.getName()) {
      case "handle" -> this.handleLocation;
      case "stick" -> this.coreLocation;
      default -> null;
    };
  }
}
