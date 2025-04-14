package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class ResearchCategory(val title: String, val index: Float) {
  companion object{
    val CODEC = RecordCodecBuilder.create {
      it.group(
        Codec.STRING.fieldOf("title").forGetter(ResearchCategory::title),
        Codec.FLOAT.fieldOf("index").forGetter(ResearchCategory::index),
      ).apply(it, ::ResearchCategory)
    }
  }
}
