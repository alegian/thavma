package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.scanning.ScanResult
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.neoforged.neoforge.network.handling.IPayloadContext

// S2C
class ScanResultPayload(
  private val scanResult: ScanResult,
) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<ScanResultPayload>(rl("scan_result"))

    val STREAM_CODEC = StreamCodec.composite(
      ScanResult.STREAM_CODEC,
      ScanResultPayload::scanResult,
      ::ScanResultPayload
    )

    fun handle(payload: ScanResultPayload, context: IPayloadContext) {
      val player = context.player()
      val soundEvent = when (payload.scanResult) {
        ScanResult.SUCCESS -> SoundEvents.PLAYER_LEVELUP
        else -> SoundEvents.LEVER_CLICK
      }
      player.level().playSound(player, player.blockPosition(), soundEvent, SoundSource.PLAYERS, 0.4f, 1f)
    }
  }
}