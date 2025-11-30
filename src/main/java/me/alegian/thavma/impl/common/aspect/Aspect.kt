package me.alegian.thavma.impl.common.aspect

import me.alegian.thavma.impl.init.registries.T7Registries.ASPECT
import net.minecraft.Util
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation

class Aspect(var id: String, var color: Int, val isPrimal: Boolean){
  val translationId by lazy {
    Util.makeDescriptionId(ASPECT.key().location().path, ASPECT.getKey(this))
  }
  val resourceKey
    get() = ASPECT.getResourceKey(this).get()

  companion object {
    val STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
      { s -> ASPECT[ResourceLocation.parse(s)]!! },
      { a -> ASPECT.getKey(a).toString() }
    )
    val CODEC = ASPECT.byNameCodec()
  }

  fun defaultStack() = AspectStack.of(this, 1)
}
