package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class ResearchEntry(val title: String) {
  companion object{
    val CODEC = RecordCodecBuilder.create {
      it.group(
        Codec.STRING.fieldOf("title").forGetter(ResearchEntry::title),
      ).apply(it, ::ResearchEntry)
    }
  }
}
