package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

data class ResearchState(val researchEntry: ResourceKey<ResearchEntry>, val socketStates: List<SocketState>, val completed: Boolean) {
  fun changeSocket(newState: SocketState): ResearchState {
    val newSocketStates = socketStates.toMutableList()
    newSocketStates.removeIf { it.indices == newState.indices }
    newSocketStates.add(newState)
    // TODO: check completion
    return ResearchState(researchEntry, newSocketStates, completed)
  }

  companion object {
    val CODEC = RecordCodecBuilder.create { builder ->
      builder.group(
        ResourceKey.codec(T7DatapackRegistries.RESEARCH_ENTRY).fieldOf("researchEntry").forGetter(ResearchState::researchEntry),
        SocketState.CODEC.listOf().fieldOf("socketStates").forGetter(ResearchState::socketStates),
        Codec.BOOL.fieldOf("completed").forGetter(ResearchState::completed)
      ).apply(builder, ::ResearchState)
    }

    val STREAM_CODEC = StreamCodec.composite(
      ResourceKey.streamCodec(T7DatapackRegistries.RESEARCH_ENTRY), ResearchState::researchEntry,
      SocketState.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchState::socketStates,
      ByteBufCodecs.BOOL, ResearchState::completed,
      ::ResearchState
    )
  }
}