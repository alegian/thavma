package me.alegian.thavma.impl.common.util

/**
 * helper to not confuse X with Y
 */
data class Indices(val row: Int, val col: Int) {
  val vec2 = vec2(col, row) // used in renderers
  val pair = Pair(row, col) // used to dereference some maps

  /**
   * Brilliant resource on this https://www.redblobgames.com/grids/hexagons/
   * Converts oddq indices to hexagonal indices
   * Used in research renderer
   */
  val axial by lazy { Indices(row - (col - (col and 1)) / 2, col) }

  operator fun plus(other: Indices) = Indices(row + other.row, col + other.col)
}