package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.T7AttributeModifiers
import me.alegian.thavma.impl.init.registries.deferred.T7ArmorMaterials
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.rl
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemAttributeModifiers
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class GogglesItem : ArmorItem(
    T7ArmorMaterials.GOGGLES, Type.HELMET, Properties()
        .durability(Type.HELMET.getDurability(15))
        .attributes(
            ItemAttributeModifiers.builder().add(
                REVEALING,
                T7AttributeModifiers.Revealing.GOGGLES,
                EquipmentSlotGroup.HEAD
            ).build()
        )
), GeoItem {
    private val cache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(controllers: ControllerRegistrar) {
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
        return this.cache
    }

    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(object : GeoRenderProvider {
            private var renderer: GeoArmorRenderer<*>? = null

            override fun <T : LivingEntity> getGeoArmorRenderer(livingEntity: T?, itemStack: ItemStack, equipmentSlot: EquipmentSlot?, original: HumanoidModel<T>?): HumanoidModel<*>? {
                if (this.renderer == null) this.renderer = GeoArmorRenderer(DefaultedItemGeoModel<GogglesItem>(rl("goggles_armor")))

                return this.renderer
            }
        })
    }
}
