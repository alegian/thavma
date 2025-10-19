package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.item.HammerItem
import me.alegian.thavma.impl.rl
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

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

    fun handle(payload: HammerPayload, context: IPayloadContext) {
      val player = context.player()
      if (player !is ServerPlayer) return
      HammerItem.breakAoE(payload.pos, payload.direction, player)
    }
  }
}