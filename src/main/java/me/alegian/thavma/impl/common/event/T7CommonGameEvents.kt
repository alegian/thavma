package me.alegian.thavma.impl.common.event

import me.alegian.thavma.impl.common.enchantment.ShriekResistance
import me.alegian.thavma.impl.common.entity.EntityHelper
import me.alegian.thavma.impl.common.item.HammerItem
import me.alegian.thavma.impl.init.registries.T7AttributeModifiers
import me.alegian.thavma.impl.init.registries.T7AttributeModifiers.Revealing.GOGGLES_CURIO
import me.alegian.thavma.impl.init.registries.deferred.T7Attributes.REVEALING
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.MobEffectEvent
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent
import top.theillusivec4.curios.api.event.CurioChangeEvent
import kotlin.math.max
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS as KFF_GAME_BUS

private var allowHammerBreakEvents = true

private fun entityTickPre(event: EntityTickEvent.Pre) {
  val livingEntity = event.entity
  if (livingEntity !is LivingEntity) return

  val attribute = livingEntity.getAttribute(Attributes.STEP_HEIGHT)
    ?: return

  val hasStepHeightFromOtherModifier =
    attribute.value >= 1.0 && !attribute.hasModifier(T7AttributeModifiers.StepHeight.LOCATION)

  if (!EntityHelper.isEntityWearingBoots(livingEntity) || hasStepHeightFromOtherModifier || livingEntity.isCrouching())
    attribute.removeModifier(
      T7AttributeModifiers.StepHeight.MODIFIER
    )
  else attribute.addOrUpdateTransientModifier(T7AttributeModifiers.StepHeight.MODIFIER)

}

private fun livingDamagePost(event: LivingDamageEvent.Post) {
  val itemStack = event.source.weaponItem ?: return
  if (itemStack.item != T7Items.ARCANUM_KATANA.get()) return

  val entity = event.entity
  if (entity.health <= 10) {
    entity.kill()
    val serverPlayer = event.source.entity
    if (serverPlayer !is ServerPlayer) return

    entity.level().playSound(
      null,
      serverPlayer.blockPosition(),
      SoundEvents.ENDER_DRAGON_GROWL,
      SoundSource.PLAYERS,
      1.0f,
      1.0f
    )
  }
}

private fun breakBlock(event: BreakEvent) {
  val player = event.player
  val itemStack = player.mainHandItem
  val item = itemStack.item
  val level = event.level

  if (player is ServerPlayer && item is HammerItem) {
    // disallow nested hammer break events, to avoid infinite recursion
    if (!allowHammerBreakEvents) return
    allowHammerBreakEvents = false

    item.tryBreak3x3exceptOrigin(player, level, itemStack)

    allowHammerBreakEvents = true
  }
}

private fun mobEffectApplicable(event: MobEffectEvent.Applicable) {
  val effectInstance = event.effectInstance ?: return
  if (effectInstance.effect.value() !== MobEffects.DARKNESS.value()) return
  if (!EntityHelper.isWearingCurio(event.entity, T7Items.DAWN_CHARM.get())) return
  event.result = MobEffectEvent.Applicable.Result.DO_NOT_APPLY
}

private fun preLivingDamage(event: LivingDamageEvent.Pre) {
  val serverLevel = event.entity.level()
  if (serverLevel !is ServerLevel) return

  val sonicType =
    serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).get(
      DamageTypes.SONIC_BOOM
    )
  if (event.source.type() != sonicType) return

  val damageBlocked = ShriekResistance.getDamageProtection(serverLevel, event.entity, event.source)
  if (damageBlocked <= 0.0f) return

  // ideally would want to apply a reduction here,
  // but warden bypasses all reductions...
  event.container.newDamage = max(0.0, (event.newDamage - damageBlocked).toDouble()).toFloat()
}

private fun curioAttributeModifiers(event: CurioAttributeModifierEvent) {
  if (event.itemStack.item == T7Items.GOGGLES_CURIO.get())
    event.addModifier(REVEALING, GOGGLES_CURIO)
}

private fun curioChange(event: CurioChangeEvent) {
  if (event.to.item == T7Items.DAWN_CHARM.get())
    event.entity.removeEffect(MobEffects.DARKNESS)
}

fun registerCommonGameEvents() {
  KFF_GAME_BUS.addListener(::entityTickPre)
  KFF_GAME_BUS.addListener(::livingDamagePost)
  KFF_GAME_BUS.addListener(::breakBlock)
  KFF_GAME_BUS.addListener(::mobEffectApplicable)
  KFF_GAME_BUS.addListener(::preLivingDamage)
  KFF_GAME_BUS.addListener(::curioAttributeModifiers)
  KFF_GAME_BUS.addListener(::curioChange)
}