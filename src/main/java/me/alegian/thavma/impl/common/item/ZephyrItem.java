package me.alegian.thavma.impl.common.item;

import me.alegian.thavma.impl.init.registries.T7Tiers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static me.alegian.thavma.impl.ThavmaKt.rl;

public class ZephyrItem extends SwordItem implements GeoItem {
  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  public ZephyrItem() {
    super(T7Tiers.INSTANCE.getARCANUM_TIER(),
        new Item.Properties().fireResistant().attributes(
            SwordItem.createAttributes(
                T7Tiers.INSTANCE.getARCANUM_TIER(),
                3, -2.4f
            )
        ));
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return this.cache;
  }

  @Override
  public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
    consumer.accept(new GeoRenderProvider() {
      private GeoItemRenderer<?> renderer;

      @Override
      public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        if (this.renderer == null)
          this.renderer = new GeoItemRenderer<>(new DefaultedItemGeoModel<ZephyrItem>(rl("zephyr")));

        return this.renderer;
      }
    });
  }
}
