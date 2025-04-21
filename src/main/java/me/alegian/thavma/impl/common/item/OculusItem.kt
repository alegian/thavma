package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.T7AttributeModifiers.Revealing.OCULUS
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.rl
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil.getHitResultOnViewVector
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.model.DefaultedItemGeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class OculusItem(props: Properties) : Item(
  props.attributes(
    ItemAttributeModifiers.builder().add(
      REVEALING,
      OCULUS,
      EquipmentSlotGroup.MAINHAND
    ).build()
  ).stacksTo(1)
), GeoItem {
  private val cache: AnimatableInstanceCache? = GeckoLibUtil.createInstanceCache(this)

  override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?> {
    val hitResult = getHitResultOnViewVector(
      player, { entity -> !entity.isSpectator && entity.isPickable }, player.blockInteractionRange()
    )
    if (hitResult.type == HitResult.Type.MISS) return InteractionResultHolder.pass(player.getItemInHand(hand))

    player.startUsingItem(hand)
    return InteractionResultHolder.success(player.getItemInHand(hand))
  }

  override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
    if(remainingUseDuration % 4 == 3 && level.isClientSide)
      level.playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.4f, 1f)
    if(remainingUseDuration == 1)
      releaseUsing(stack, level, livingEntity, getUseDuration(stack, livingEntity))
  }

  override fun getUseAnimation(itemStack: ItemStack): UseAnim {
    return UseAnim.CUSTOM
  }

  override fun getUseDuration(pStack: ItemStack, pEntity: LivingEntity): Int {
    return 40
  }

  override fun releaseUsing(itemStack: ItemStack, level: Level, entity: LivingEntity, timeCharged: Int) {
    if (level.isClientSide && entity is Player && timeCharged == getUseDuration(itemStack, entity)) {
      level.playSound(entity, entity.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.4f, 1f)
    }
  }

  override fun registerControllers(controllers: ControllerRegistrar?) {
  }

  override fun getAnimatableInstanceCache(): AnimatableInstanceCache? {
    return this.cache
  }

  override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider?>) {
    consumer.accept(object : GeoRenderProvider {
      private val renderer by lazy {
        GeoItemRenderer<OculusItem>(object : DefaultedItemGeoModel<OculusItem>(rl("oculus")) {
          override fun getRenderType(animatable: OculusItem, texture: ResourceLocation): RenderType {
            return RenderType.entityTranslucent(texture)
          }
        })
      }

      override fun getGeoItemRenderer(): BlockEntityWithoutLevelRenderer {
        return renderer
      }
    })
  }
}
