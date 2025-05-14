package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.common.util.Indices
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelAccessor
import java.util.*

/**
 * The state which is held by Research Scrolls
 */
data class ResearchState(
  val researchEntry: ResourceKey<ResearchEntry>,
  val socketStates: List<SocketState>,
  val completed: Boolean
) {
  /**
   * mutate a single socket and recalculate completion status
   */
  fun changeSocket(level: LevelAccessor, newState: SocketState): ResearchState {
    var newSocketStates = socketStates.toMutableList()
    newSocketStates.removeIf { it.indices == newState.indices }
    newSocketStates.add(newState)

    val isCompleted = level.registryAccess().registry(T7DatapackRegistries.RESEARCH_ENTRY).get().get(researchEntry)?.let {
      calculateCompleted(it.defaultResearchState, newSocketStates, newState)
    } ?: false
    if (isCompleted) newSocketStates = newSocketStates.map { it.withLocked(true) }.toMutableList()

    return ResearchState(researchEntry, newSocketStates, isCompleted)
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
      val connected = currAspect.components.map { it.get() }.contains(neighborAspect)
          || neighborAspect.components.map { it.get() }.contains(currAspect)
      if (!connected) continue

      connections.add(neighborIdx)
      toExplore.add(neighborIdx)
    }
  }

  return connections.containsAll(defaultPositions)
}
