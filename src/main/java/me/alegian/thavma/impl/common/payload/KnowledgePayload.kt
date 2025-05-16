package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.client.researchToast
import me.alegian.thavma.impl.common.entity.setKnowledge
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * S2C
 * firstPacket is used to not show "research complete" toasts on the first sync
 * which happens every login
 */
class KnowledgePayload(val newKnowledge: List<ResourceKey<ResearchEntry>>, val firstPacket: Boolean = false) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<KnowledgePayload>(rl("knowledge"))

    val STREAM_CODEC = StreamCodec.composite(
      ResourceKey.streamCodec(T7DatapackRegistries.RESEARCH_ENTRY).apply(ByteBufCodecs.list()),
      KnowledgePayload::newKnowledge,
      ByteBufCodecs.BOOL,
      KnowledgePayload::firstPacket,
      ::KnowledgePayload
    )

    fun handle(payload: KnowledgePayload, context: IPayloadContext) {
      val player = context.player()
      player.setKnowledge(payload.newKnowledge)

      if (payload.firstPacket) return
      for (research in payload.newKnowledge)
        researchToast(research)
    }
  }
}