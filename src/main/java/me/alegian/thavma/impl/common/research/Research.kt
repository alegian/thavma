package me.alegian.thavma.impl.common.research

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

class Research(val title: String) {
  companion object{
    val CODEC = RecordCodecBuilder.create {
      it.group(
        Codec.STRING.fieldOf("title").forGetter(Research::title),
      ).apply(it, ::Research)
    }
  }
}
