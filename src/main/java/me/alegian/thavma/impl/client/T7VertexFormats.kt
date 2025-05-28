package me.alegian.thavma.impl.client

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement

object T7VertexFormats {
  var CENTER = registerFormatElement(VertexFormatElement.Type.FLOAT, 3)
  var SCALE = registerFormatElement(VertexFormatElement.Type.FLOAT, 1)
  var AURA_NODE = VertexFormat.builder()
      .add("Position", VertexFormatElement.POSITION)
      .add("Color", VertexFormatElement.COLOR)
      .add("Center", CENTER)
      .add("Scale", SCALE)
      .build()

  private fun registerFormatElement(type: VertexFormatElement.Type, count: Int): VertexFormatElement {
    // the first 5 ids are vanilla
    for (i in 6..31) try {
      return VertexFormatElement.register(i, 0, type, VertexFormatElement.Usage.GENERIC, count)
    } catch (_: IllegalArgumentException) {
    }

    throw RuntimeException("Thavma Exception: Failed to register vertex format element")
  }
}
