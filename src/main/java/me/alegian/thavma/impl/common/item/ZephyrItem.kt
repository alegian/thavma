package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.T7Tiers.THAVMITE_TIER
import me.alegian.thavma.impl.rl
import net.minecraft.world.item.SwordItem
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class ZephyrItem : SwordItem(
  THAVMITE_TIER,
  Properties().fireResistant().attributes(
    createAttributes(
      THAVMITE_TIER,
      3, -2.4f
    )
  )
), GeoItem {
  private val cache = GeckoLibUtil.createInstanceCache(this)

  override fun registerControllers(controllers: ControllerRegistrar) {
  }

  override fun getAnimatableInstanceCache() = cache

  override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
    consumer.accept(object : GeoRenderProvider {
      private val renderer by lazy{
        GeoItemRenderer(DefaultedItemGeoModel<ZephyrItem>(rl("zephyr")))
      }

      override fun getGeoItemRenderer() = renderer
    })
  }
}
