package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.entity.tryScan
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.neoforged.neoforge.network.handling.IPayloadContext

// S2C
class ScanPayload(val newScans: List<String>): CustomPacketPayload {
  override fun type() = TYPE

  companion object{
    val TYPE = CustomPacketPayload.Type<ScanPayload>(rl("scan"))

    val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      ScanPayload::newScans,
      ::ScanPayload
    )

    fun handle(payload: ScanPayload, context: IPayloadContext){
      val player = context.player()
      player.tryScan(payload.newScans)
      player.level().playSound(player, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.4f, 1f)
    }
  }
}