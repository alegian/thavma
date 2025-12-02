package me.alegian.thavma.impl.common.scanning

import com.google.common.primitives.Doubles.max
import me.alegian.thavma.impl.common.aspect.AspectHelper
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.relatedAspects
import me.alegian.thavma.impl.common.entity.addKnowledge
import me.alegian.thavma.impl.common.entity.knowsAspect
import me.alegian.thavma.impl.common.payload.ScanResultPayload
import me.alegian.thavma.impl.common.util.serialize
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.network.PacketDistributor

private fun Player.hasScanned(key: ResourceKey<*>) = getData(T7Attachments.SCANNED).scanned.contains(key.serialize())

// itemEntities fall back to items
fun Player.hasScanned(entity: Entity): Boolean {
  if (entity is ItemEntity) return hasScanned(entity.item)
  return hasScanned(entityKey(entity.type))
}

// blocks fall back to items
fun Player.hasScanned(blockState: BlockState): Boolean {
  return hasScanned(itemKey(blockState.block.asItem()))
}

fun Player.hasScanned(itemStack: ItemStack): Boolean {
  return hasScanned(itemKey(itemStack.item))
}

private fun ServerPlayer.tryScan(key: ResourceKey<*>, aspectMap: AspectMap?) {
  var scanResult = ScanResult.SUCCESS
  if (aspectMap == null || aspectMap.isEmpty) scanResult = ScanResult.UNSUPPORTED
  else if (hasScanned(key)) scanResult = ScanResult.SUCCESS
  else {
    val aspects = aspectMap.map { it.aspect }
    if (aspects.any { it.wrapAsHolder().relatedAspects().none { !knowsAspect(it) } }) scanResult = ScanResult.LOCKED
    else
      addKnowledge(
        aspects
          .filter { !knowsAspect(it) }
          .map { it.resourceKey }
      )
  }
  if (scanResult != ScanResult.SUCCESS) return
  val old = getData(T7Attachments.SCANNED)
  old.scanned.add(key.serialize())
  setData(T7Attachments.SCANNED, old)

  PacketDistributor.sendToPlayer(this, ScanResultPayload(scanResult))
}

// itemEntities fall back to items
fun ServerPlayer.tryScan(entity: Entity) {
  if (entity is ItemEntity) return tryScan(itemKey(entity.item.item), AspectHelper.getAspects(entity.item))

  tryScan(entityKey(entity.type), AspectHelper.getAspects(entity))
}

fun ServerPlayer.tryScan(blockState: BlockState) {
  tryScan(itemKey(blockState.block.asItem()), AspectHelper.getAspects(blockState.block))
}

private fun entityKey(entityType: EntityType<*>) =
  BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).get()

private fun itemKey(item: Item) =
  BuiltInRegistries.ITEM.getResourceKey(item).get()

fun Player.getScanHitResult(): HitResult {
  val rayVec = getViewVector(0.0f).scale(max(blockInteractionRange(), entityInteractionRange()))
  val predicate = { entity: Entity -> !entity.isSpectator && (entity.isPickable || entity is ItemEntity) }
  val trueHitResult = ProjectileUtil.getHitResult(eyePosition, this, predicate, rayVec, level(), 0.0f, ClipContext.Block.OUTLINE)

  val range =
    if (trueHitResult is BlockHitResult) blockInteractionRange()
    else entityInteractionRange()

  val valid = trueHitResult.location.closerThan(eyePosition, range)

  return if (valid) trueHitResult else BlockHitResult.miss(eyePosition, direction, blockPosition())
}