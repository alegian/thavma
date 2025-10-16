package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.item.HammerItem
import me.alegian.thavma.impl.rl
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.UUID

// C2S
class HammerPayload(
  val pos: BlockPos,
  val direction: Direction
) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<HammerPayload>(rl("hammer"))

    val STREAM_CODEC = StreamCodec.composite(
      BlockPos.STREAM_CODEC,
      HammerPayload::pos,
      Direction.STREAM_CODEC,
      HammerPayload::direction,
      ::HammerPayload
    )

    /*
     * Keep track of last hammered positions to avoid
     * chain reactions of hammers causing hammers
     */
    private val lastHammers = mutableMapOf<UUID, MutableList<BlockPos>>()
    fun handle(payload: HammerPayload, context: IPayloadContext) {
      val player = context.player()
      if (player !is ServerPlayer) return
      if (lastHammers[player.uuid]?.contains(payload.pos) ?: false) return

      val itemStack = player.mainHandItem
      val item = itemStack.item

      if (item is HammerItem) {
        val positions = item.getValid3x3PositionsExceptOrigin(payload.pos, payload.direction, player.level(), itemStack, player)
        for (pos in positions)
          player.gameMode.destroyBlock(pos)

        positions.add(payload.pos)
        lastHammers[player.uuid] = positions
      }
    }

    /*
     * Allows place & break again.
     * Would otherwise not work due to lastHammers
     */
    fun placeBlock(event: BlockEvent.EntityPlaceEvent) {
      lastHammers[event.entity?.uuid]?.remove(event.pos)
    }
  }
}