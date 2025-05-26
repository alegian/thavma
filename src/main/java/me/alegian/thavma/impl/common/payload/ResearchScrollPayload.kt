package me.alegian.thavma.impl.common.payload

import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.common.research.ResearchState
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import me.alegian.thavma.impl.rl
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.items.ItemHandlerHelper.giveItemToPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.jvm.optionals.getOrNull

// C2S
class ResearchScrollPayload(val entry: ResourceKey<ResearchEntry>) : CustomPacketPayload {
  override fun type() = TYPE

  companion object {
    val TYPE = CustomPacketPayload.Type<ResearchScrollPayload>(rl("research_scroll"))

    val STREAM_CODEC = StreamCodec.composite(
      ResourceKey.streamCodec(T7DatapackRegistries.RESEARCH_ENTRY),
      ResearchScrollPayload::entry,
      ::ResearchScrollPayload
    )

    fun handle(payload: ResearchScrollPayload, context: IPayloadContext) {
      val player = context.player()
      val stack = T7Items.RESEARCH_SCROLL.toStack()
      val registry = player.level().registryAccess().registry(T7DatapackRegistries.RESEARCH_ENTRY).getOrNull()
        ?: return
      val defaultState = registry.get(payload.entry)?.defaultResearchState ?: return
      stack.set(T7DataComponents.RESEARCH_STATE, ResearchState(payload.entry, defaultState, false))

      giveItemToPlayer(player, stack)
    }
  }
}