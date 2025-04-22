package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.common.payload.ScanPayload
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import me.alegian.thavma.impl.init.registries.deferred.T7Items.ARCANUM_KATANA
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.ItemTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.network.PacketDistributor


object EntityHelper {
  fun isEntityWearingBoots(entity: LivingEntity): Boolean {
    val boots = entity.getItemBySlot(EquipmentSlot.FEET)
    return boots.`is`(ItemTags.FOOT_ARMOR)
  }

  fun invertSwingingArm(pLivingEntity: LivingEntity) {
    if (pLivingEntity.swingingArm == InteractionHand.MAIN_HAND)
      pLivingEntity.swingingArm = InteractionHand.OFF_HAND
    else pLivingEntity.swingingArm = InteractionHand.MAIN_HAND
  }

  fun isHandKatana(hand: InteractionHand): Boolean {
    return Minecraft.getInstance().player?.getItemInHand(hand)?.item == ARCANUM_KATANA.get()
  }

  fun getServerHitResult(player: ServerPlayer): BlockHitResult {
    return player.level().clip(
      ClipContext(
        player.getEyePosition(1f),
        player.getEyePosition(1f).add(
          player.getViewVector(1f).scale(6.0)
        ),
        ClipContext.Block.COLLIDER,
        ClipContext.Fluid.NONE,
        player
      )
    )
  }

  private fun Player.hasScanned(loc: ResourceLocation) = getData(T7Attachments.SCANNED).scanned.contains(loc)

  // itemEntities fall back to items
  fun Player.hasScanned(entity: Entity): Boolean {
    if (entity is ItemEntity) return hasScanned(entity.item)
    return hasScanned(BuiltInRegistries.ENTITY_TYPE.getKey(entity.type))
  }

  fun Player.hasScanned(blockState: BlockState): Boolean {
    return hasScanned(blockState.block)
  }

  // blocks fall back to items if possible
  fun Player.hasScanned(block: Block): Boolean {
    val item = block.asItem()
    if (item != Items.AIR) return hasScanned(BuiltInRegistries.ITEM.getKey(item))
    return hasScanned(BuiltInRegistries.BLOCK.getKey(block))
  }

  fun Player.hasScanned(itemStack: ItemStack): Boolean {
    return hasScanned(BuiltInRegistries.ITEM.getKey(itemStack.item))
  }

  fun Player.setScanned(newScans: List<ResourceLocation>) {
    val old = getData(T7Attachments.SCANNED)
    old.scanned.addAll(newScans)
    setData(T7Attachments.SCANNED, old)

    if (this is ServerPlayer)
      PacketDistributor.sendToPlayer(this, ScanPayload(newScans))
  }

  fun Player.setScanned(loc: ResourceLocation) {
    setScanned(listOf(loc))
  }

  // itemEntities fall back to items
  fun ServerPlayer.setScanned(entity: Entity) {
    if (entity is ItemEntity) return setScanned(entity.item)

    setScanned(BuiltInRegistries.ENTITY_TYPE.getKey(entity.type))
  }

  // blocks fall back to items if possible
  fun ServerPlayer.setScanned(blockState: BlockState) {
    val item = blockState.block.asItem()
    if (item != Items.AIR) {
      setScanned(BuiltInRegistries.ITEM.getKey(item))
    } else {
      setScanned(BuiltInRegistries.BLOCK.getKey(blockState.block))
    }
  }

  fun ServerPlayer.setScanned(itemStack: ItemStack) {
    setScanned(BuiltInRegistries.ITEM.getKey(itemStack.item))
  }
}
