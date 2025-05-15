package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.entity.setKnowledge
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

// S2C
class KnowledgePayload(val newKnowledge: List<String>): CustomPacketPayload {
  override fun type() = TYPE

  companion object{
    val TYPE = CustomPacketPayload.Type<KnowledgePayload>(rl("knowledge"))

    val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      KnowledgePayload::newKnowledge,
      ::KnowledgePayload
    )

    fun handle(payload: KnowledgePayload, context: IPayloadContext){
      val player = context.player()
      player.setKnowledge(payload.newKnowledge)
    }
  }
}