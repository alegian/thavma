package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.entity.EntityHelper.setScanned
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

class ScanPayload(val newScans: List<ResourceLocation>): CustomPacketPayload {
  override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
    return TYPE
  }

  companion object{
    val TYPE = CustomPacketPayload.Type<ScanPayload>(rl("scan"))

    val STREAM_CODEC = StreamCodec.composite(
      ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
      ScanPayload::newScans,
      ::ScanPayload
    )

    fun handle(payload: ScanPayload, context: IPayloadContext){
      val player = context.player()
      player.setScanned(payload.newScans)
    }
  }
}