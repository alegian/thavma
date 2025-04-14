package me.alegian.thavma.impl.client.gui.research_table

import me.alegian.thavma.impl.common.util.vec2
import net.minecraft.world.phys.Vec2

const val HEX_GRID_GAP = 2

// brilliant resource on this https://www.redblobgames.com/grids/hexagons/
fun axial(oddqIdx: Vec2): Vec2 {
  val col = oddqIdx.x.toInt()
  val row = oddqIdx.y.toInt()
  val q = col
  val r = row - (col - (col and 1)) / 2
  return vec2(q, r)
}

val axialNeighbors = listOf(
  vec2(1, 0),
  vec2(0, 1),
  vec2(-1, 0),
  vec2(0, -1),
  vec2(-1, 1),
  vec2(1, -1),
)

// used to dereference some hashmaps
fun Vec2.toIntPair() = Pair(x.toInt(), y.toInt())