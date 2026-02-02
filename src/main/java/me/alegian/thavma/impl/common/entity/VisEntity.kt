package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DATAGEN_PRIMALS
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.*

private const val PLAYER_TAG: String = "player"
private const val PERIOD_TICKS = 4
/**
 * Entity that renders as a spiral beam between the Player and an Aura Node.
 * Transfers vis from the target block to the held wand by ticking.
 */
class VisEntity(pLevel: Level, player: Player?, blockPos: BlockPos = BlockPos(0, 0, 0)) : RendererEntity(pLevel, blockPos.center) {
  private var playerUUID: UUID? = null // save player UUID and not entire Player, because when deserializing, level.players is not yet populated

  init {
    if (player != null) this.playerUUID = player.uuid
  }

  /**
   * Ticks once every 5 ticks. Kills itself if the player is not using the Wand.
   * Charges the held aspect container (wand).
   * Interrupts item use when the aspect container is full.
   */
  override fun tick() {
    if (tickCount % PERIOD_TICKS != 0) return

    var transferPair: AspectContainer.Pair? = null
    player?.let {
      transferPair = AspectContainer.blockSourceItemSink(level(), blockPosition(), it.useItem)
      val canTransfer = transferPair?.canTransferPrimals() ?: false
      if (!canTransfer) it.stopUsingItem()
    }

    this.serverTick(transferPair)
  }

  private fun serverTick(pair: AspectContainer.Pair?) {
    val level = level()?: return
    if (level.isClientSide() || level !is ServerLevel) return
    val currPlayer = player
    if (currPlayer == null || !currPlayer.isUsingItem || pair == null) {
      kill()
      return
    }
    val transferred = pair.transferPrimal((this.tickCount / PERIOD_TICKS) % DATAGEN_PRIMALS.size, 1)
    if (transferred != null && transferred.amount > 0) level.updateBlockEntityS2C(blockPosition())
  }

  val player
    get() = playerUUID?.let { level().getPlayerByUUID(it) }

  override fun readAdditionalSaveData(pCompound: CompoundTag) {
    if (pCompound.hasUUID(PLAYER_TAG)) this.playerUUID = pCompound.getUUID(PLAYER_TAG)
  }

  override fun addAdditionalSaveData(pCompound: CompoundTag) {
    playerUUID?.let { pCompound.putUUID(PLAYER_TAG, it) }
  }

  override fun restoreFrom(pEntity: Entity) {
    super.restoreFrom(pEntity)
    this.playerUUID = (pEntity as VisEntity).playerUUID
  }

  override fun getAddEntityPacket(pEntity: ServerEntity): Packet<ClientGamePacketListener> {
    val player = this.player
    return ClientboundAddEntityPacket(this, pEntity, player?.id ?: 0)
  }

  override fun recreateFromPacket(pPacket: ClientboundAddEntityPacket) {
    super.recreateFromPacket(pPacket)
    val entity = level().getEntity(pPacket.data)
    if (entity is Player) this.playerUUID = entity.getUUID()
  }
}
