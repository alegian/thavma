package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.client.researchToast
import me.alegian.thavma.impl.common.codec.listOf
import me.alegian.thavma.impl.common.entity.addKnowledge
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

// S2C
class ResearchToastPayload(private val newKnowledge: List<String>) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<ResearchToastPayload>(rl("research_toast"))

    val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8.listOf(),
      ResearchToastPayload::newKnowledge,
      ::ResearchToastPayload
    )

    fun handle(payload: ResearchToastPayload, context: IPayloadContext) {
      for (entry in payload.newKnowledge) {
        val rawRL = entry.split('.').drop(1).joinToString("")
        val rl = ResourceLocation.parse(rawRL)
        val entry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.get(rl)
        if (entry != null) researchToast(entry)
      }
    }
  }
}