package me.alegian.thavma.impl.common.util

/**
 * helper to not confuse X with Y
 */
data class Dimensions(val rows: Int, val cols: Int) {
  val vec2 = vec2(cols, rows) // used in renderers
}