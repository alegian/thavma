package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class ResearchCategory(val title: String) {
  companion object{
    val CODEC = RecordCodecBuilder.create {
      it.group(
        Codec.STRING.fieldOf("title").forGetter(ResearchCategory::title),
      ).apply(it, ::ResearchCategory)
    }
  }
}
