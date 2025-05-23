package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.deferred.T7ArmorMaterials.THAVMITE_VANGUARD
import me.alegian.thavma.impl.rl
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class ThavmiteVanguardArmorItem(type: Type, properties: Properties) : ArmorItem(THAVMITE_VANGUARD, type, properties), GeoItem {
  private val cache = GeckoLibUtil.createInstanceCache(this)

  override fun registerControllers(controllers: ControllerRegistrar) {
  }

  override fun getAnimatableInstanceCache() = cache

  override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
    consumer.accept(object : GeoRenderProvider {
      private val renderer by lazy {
        GeoArmorRenderer(DefaultedItemGeoModel<ThavmiteVanguardArmorItem>(rl("thavmite_vanguard_armor")))
      }

      override fun <T : LivingEntity> getGeoArmorRenderer(livingEntity: T?, itemStack: ItemStack, equipmentSlot: EquipmentSlot?, original: HumanoidModel<T>?) = renderer
    })
  }
}
