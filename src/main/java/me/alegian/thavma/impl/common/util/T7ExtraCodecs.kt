package me.alegian.thavma.impl.common.util

import com.mojang.serialization.Codec
import net.minecraft.Util
import org.joml.Vector2i

object T7ExtraCodecs{
  val VECTOR2I: Codec<Vector2i> = Codec.INT
    .listOf()
    .comapFlatMap(
      { intList -> Util.fixedSize(intList, 2).map{ list -> Vector2i(list[0], list[1]) } },
      { vec -> listOf(vec.x, vec.y) }
    )
}