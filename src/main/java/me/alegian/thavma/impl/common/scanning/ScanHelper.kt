package me.alegian.thavma.impl.common.scanning

import com.google.common.primitives.Doubles.max
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.getAspects
import me.alegian.thavma.impl.common.entity.knowsAspect
import me.alegian.thavma.impl.common.entity.tryLearnAspects
import me.alegian.thavma.impl.common.payload.ScanPayload
import me.alegian.thavma.impl.common.util.serialize
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.core.registries.BuiltInRegistries
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

private fun Player.hasScanned(key: String) = getData(T7Attachments.SCANNED).scanned.contains(key)

// itemEntities fall back to items
fun Player.hasScanned(entity: Entity): Boolean {
  if (entity is ItemEntity) return hasScanned(entity.item)
  return hasScanned(entityScanKey(entity.type))
}

// blocks fall back to items
fun Player.hasScanned(blockState: BlockState): Boolean {
  return hasScanned(itemScanKey(blockState.block.asItem()))
}

fun Player.hasScanned(itemStack: ItemStack): Boolean {
  return hasScanned(itemScanKey(itemStack.item))
}

fun Player.handleScanResult(scanResult: ScanResult, newScans: List<String>, firstPacket: Boolean = false) {
  if (scanResult == ScanResult.SUCCESS) {
    val old = getData(T7Attachments.SCANNED)
    old.scanned.addAll(newScans)
    setData(T7Attachments.SCANNED, old)
  }

  if (this is ServerPlayer)
    PacketDistributor.sendToPlayer(this, ScanPayload(scanResult, newScans, firstPacket))
}

fun Player.tryScan(key: String, aspectMap: AspectMap?) {
  var scanResult = ScanResult.SUCCESS
  if (aspectMap == null || aspectMap.isEmpty) scanResult = ScanResult.UNSUPPORTED
  else if (hasScanned(key)) scanResult = ScanResult.SUCCESS
  else {
    val aspects = aspectMap.map { it.aspect }
    if (aspects.flatMap { it.components }.any { !knowsAspect(it.get()) }) scanResult = ScanResult.LOCKED
    else tryLearnAspects(aspects)
  }
  handleScanResult(scanResult, listOf(key))
}

// itemEntities fall back to items
fun ServerPlayer.tryScan(entity: Entity) {
  if (entity is ItemEntity) return tryScan(itemScanKey(entity.item.item), getAspects(entity.item))

  tryScan(entityScanKey(entity.type), getAspects(entity))
}

fun ServerPlayer.tryScan(blockState: BlockState) {
  tryScan(itemScanKey(blockState.block.asItem()), getAspects(blockState.block))
}

private fun entityScanKey(entityType: EntityType<*>): String =
  BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).get().serialize()

private fun itemScanKey(item: Item): String =
  BuiltInRegistries.ITEM.getResourceKey(item).get().serialize()

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