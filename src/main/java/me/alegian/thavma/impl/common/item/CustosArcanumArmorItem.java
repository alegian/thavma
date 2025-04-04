package me.alegian.thavma.impl.common.item;

import me.alegian.thavma.impl.init.registries.deferred.T7ArmorMaterials;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static me.alegian.thavma.impl.ThavmaKt.rl;

public class CustosArcanumArmorItem extends ArmorItem implements GeoItem {
  private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

  public CustosArcanumArmorItem(ArmorItem.Type type, Properties properties) {
    super(T7ArmorMaterials.INSTANCE.getCUSTOS_ARCANUM(), type, properties);
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
      private GeoArmorRenderer<?> renderer;

      @Override
      public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
        if (this.renderer == null)
          this.renderer = new GeoArmorRenderer<>(new DefaultedItemGeoModel<CustosArcanumArmorItem>(rl("custos_arcanum_armor")));

        return this.renderer;
      }
    });
  }
}
