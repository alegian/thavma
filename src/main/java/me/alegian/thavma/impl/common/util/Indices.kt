package me.alegian.thavma.impl.common.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * helper to not confuse X with Y
 */
data class Indices(val row: Int, val col: Int) {
  val vec2 = vec2(col, row) // used in renderers

  /**
   * Brilliant resource on this https://www.redblobgames.com/grids/hexagons/
   * Converts oddq indices to hexagonal indices
   * Used in research renderer
   */
  val axial by lazy { Indices(row - (col - (col and 1)) / 2, col) }

  operator fun plus(other: Indices) = Indices(row + other.row, col + other.col)

  companion object {
    val CODEC = RecordCodecBuilder.create { builder ->
      builder.group(
        Codec.INT.fieldOf("row").forGetter(Indices::row),
        Codec.INT.fieldOf("col").forGetter(Indices::col)
      ).apply(builder, ::Indices)
    }

    val STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, Indices::row,
      ByteBufCodecs.INT, Indices::col,
      ::Indices
    )
  }
}