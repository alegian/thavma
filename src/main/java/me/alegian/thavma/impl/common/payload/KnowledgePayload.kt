package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.client.researchToast
import me.alegian.thavma.impl.common.codec.listOf
import me.alegian.thavma.impl.common.entity.setKnowledge
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * S2C
 * firstPacket is used to not show "research complete" toasts on the first sync
 * which happens every login
 */
class KnowledgePayload(private val newKnowledge: List<String>, private val firstPacket: Boolean = false) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<KnowledgePayload>(rl("knowledge"))

    val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.listOf(),
      KnowledgePayload::newKnowledge,
      ByteBufCodecs.BOOL,
      KnowledgePayload::firstPacket,
      ::KnowledgePayload
    )

    fun handle(payload: KnowledgePayload, context: IPayloadContext) {
      val player = context.player()
      player.setKnowledge(payload.newKnowledge)

      if (payload.firstPacket) return

      for (entry in payload.newKnowledge) {
        val rawRL = entry.split('.').drop(1).joinToString("")
        val rl = ResourceLocation.parse(rawRL)
        val entry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.get(rl)
        if (entry != null) researchToast(entry)
      }
    }
  }
}