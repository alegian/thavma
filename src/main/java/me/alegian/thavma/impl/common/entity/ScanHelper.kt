package me.alegian.thavma.impl.common.entity

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
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.network.PacketDistributor

private fun Player.hasScanned(key: String) = getData(T7Attachments.SCANNED).scanned.contains(key)

// itemEntities fall back to items
fun Player.hasScanned(entity: Entity): Boolean {
  if (entity is ItemEntity) return hasScanned(entity.item)
  return hasScanned(entityScanKey(entity.type))
}

fun Player.hasScanned(blockState: BlockState): Boolean {
  return hasScanned(blockState.block)
}

// blocks fall back to items if possible
fun Player.hasScanned(block: Block): Boolean {
  val item = block.asItem()
  if (item != Items.AIR) return hasScanned(itemScanKey(item))
  return hasScanned(blockScanKey(block))
}

fun Player.hasScanned(itemStack: ItemStack): Boolean {
  return hasScanned(itemScanKey(itemStack.item))
}

fun Player.setScanned(newScans: List<String>) {
  val old = getData(T7Attachments.SCANNED)
  old.scanned.addAll(newScans)
  setData(T7Attachments.SCANNED, old)

  if (this is ServerPlayer)
    PacketDistributor.sendToPlayer(this, ScanPayload(newScans))
}

fun Player.setScanned(key: String) {
  setScanned(listOf(key))
}

// itemEntities fall back to items
fun ServerPlayer.setScanned(entity: Entity) {
  if (entity is ItemEntity) return setScanned(entity.item)

  setScanned(entityScanKey(entity.type))
}

// blocks fall back to items if possible
fun ServerPlayer.setScanned(blockState: BlockState) {
  val item = blockState.block.asItem()
  if (item != Items.AIR) {
    setScanned(itemScanKey(item))
  } else {
    setScanned(blockScanKey(blockState.block))
  }
}

fun ServerPlayer.setScanned(itemStack: ItemStack) {
  setScanned(itemScanKey(itemStack.item))
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