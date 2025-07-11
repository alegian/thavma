package me.alegian.thavma.impl.common.scanning

import com.google.common.primitives.Doubles.max
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.getAspects
import me.alegian.thavma.impl.common.entity.knowsAspect
import me.alegian.thavma.impl.common.entity.tryLearnAspects
import me.alegian.thavma.impl.common.payload.ScanPayload
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.Util
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.Block
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

// blocks fall back to items if possible
fun Player.hasScanned(blockState: BlockState): Boolean {
  val item = blockState.block.asItem()
  if (item != Items.AIR) return hasScanned(itemScanKey(item))
  return hasScanned(blockScanKey(blockState.block))
}

fun Player.hasScanned(itemStack: ItemStack): Boolean {
  return hasScanned(itemScanKey(itemStack.item))
}

fun Player.setScanned(newScans: List<String>, firstPacket: Boolean = false) {
  val old = getData(T7Attachments.SCANNED)
  old.scanned.addAll(newScans)
  setData(T7Attachments.SCANNED, old)

  if (this is ServerPlayer)
    PacketDistributor.sendToPlayer(this, ScanPayload(newScans, firstPacket))
}

fun Player.tryScan(key: String, aspectMap: AspectMap?) {
  if (aspectMap == null || aspectMap.isEmpty) return
  if (hasScanned(key)) return
  val aspects = aspectMap.map { it.aspect }
  if (aspects.flatMap { it.components }.any { !knowsAspect(it.get()) }) return
  setScanned(listOf(key))
  tryLearnAspects(aspects)
}

// itemEntities fall back to items
fun ServerPlayer.tryScan(entity: Entity) {
  if (entity is ItemEntity) return tryScan(itemScanKey(entity.item.item), getAspects(entity.item))

  tryScan(entityScanKey(entity.type), getAspects(entity))
}

fun ServerPlayer.tryScan(blockState: BlockState) {
  tryScan(blockScanKey(blockState.block), getAspects(blockState.block))
}

private fun entityScanKey(entityType: EntityType<*>): String =
  Util.makeDescriptionId(
    Registries.ENTITY_TYPE.location().path,
    BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
  )

private fun blockScanKey(block: Block): String =
  Util.makeDescriptionId(
    Registries.BLOCK.location().path,
    BuiltInRegistries.BLOCK.getKey(block)
  )

private fun itemScanKey(item: Item): String =
  Util.makeDescriptionId(
    Registries.ITEM.location().path,
    BuiltInRegistries.ITEM.getKey(item)
  )

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