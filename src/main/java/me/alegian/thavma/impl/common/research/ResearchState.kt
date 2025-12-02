package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.aspect.relatedTo
import me.alegian.thavma.impl.common.util.Indices
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFileCodec
import java.util.*

/**
 * The state which is held by Research Scrolls
 */
data class ResearchState(
  val researchEntry: Holder<ResearchEntry>,
  val socketStates: List<SocketState>,
  val completed: Boolean
) {
  /**
   * mutate a single socket and recalculate completion status
   */
  fun changeSocket(newState: SocketState): ResearchState {
    var newSocketStates = socketStates.toMutableList()
    newSocketStates.removeIf { it.indices == newState.indices }
    newSocketStates.add(newState)

    val isCompleted = calculateCompleted(researchEntry.value().defaultResearchState, newSocketStates, newState)
    if (isCompleted) newSocketStates = newSocketStates.map { it.withLocked(true) }.toMutableList()

    return ResearchState(researchEntry, newSocketStates, isCompleted)
  }

  companion object {
    val CODEC = RecordCodecBuilder.create { builder ->
      builder.group(
        RegistryFileCodec.create(T7DatapackRegistries.RESEARCH_ENTRY, ResearchEntry.CODEC).fieldOf("researchEntry").forGetter(ResearchState::researchEntry),
        SocketState.CODEC.listOf().fieldOf("socketStates").forGetter(ResearchState::socketStates),
        Codec.BOOL.fieldOf("completed").forGetter(ResearchState::completed)
      ).apply(builder, ::ResearchState)
    }

    val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.holderRegistry(T7DatapackRegistries.RESEARCH_ENTRY), ResearchState::researchEntry,
      SocketState.STREAM_CODEC.apply(ByteBufCodecs.list()), ResearchState::socketStates,
      ByteBufCodecs.BOOL, ResearchState::completed,
      ::ResearchState
    )
  }
}

/**
 * BFS traversal of sockets to determine if solved
 */
private fun calculateCompleted(defaultStates: List<SocketState>, socketStates: MutableList<SocketState>, newState: SocketState): Boolean {
  if (newState.broken) return false
  val defaultPositions = defaultStates.filter { !it.broken && it.aspect != null }.map { it.indices.axial }
  val graph = socketStates.associateBy { it.indices.axial }

  val explored = mutableSetOf<Indices>()
  val connections = mutableSetOf<Indices>()
  val toExplore = ArrayDeque<Indices>()
  toExplore.add(newState.indices.axial)

  while (toExplore.isNotEmpty()) {
    val idx = toExplore.removeFirst()
    if (!explored.add(idx)) continue

    val currSocket = graph[idx] ?: continue
    val currAspect = currSocket.aspect ?: continue

    for (neighborIdx in idx.axialNeighbors) {
      if (explored.contains(neighborIdx)) continue
      val neighbor = graph[neighborIdx] ?: continue
      val neighborAspect = neighbor.aspect ?: continue
      if (!currAspect.wrapAsHolder().relatedTo(neighborAspect.wrapAsHolder())) continue

      connections.add(neighborIdx)
      toExplore.add(neighborIdx)
    }
  }

  return connections.containsAll(defaultPositions)
}
