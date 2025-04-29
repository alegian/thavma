package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.common.util.Indices

const val HEX_GRID_GAP = 2

val axialNeighbors = listOf(
  Indices(1, 0),
  Indices(0, 1),
  Indices(-1, 0),
  Indices(0, -1),
  Indices(-1, 1),
  Indices(1, -1),
)