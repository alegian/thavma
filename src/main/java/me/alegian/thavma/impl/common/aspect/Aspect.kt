package me.alegian.thavma.impl.common.aspect

import me.alegian.thavma.impl.init.registries.T7Registries.ASPECT
import net.minecraft.Util
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class Aspect(var id: String, var color: Int, var components: List<Supplier<Aspect>>) {
  val isPrimal: Boolean
    get() = components.isEmpty()
  val translationId by lazy {
    Util.makeDescriptionId(ASPECT.key().location().path, ASPECT.getKey(this))
  }
  val rank: Int by lazy {
    if (isPrimal) 1
    else components.maxOf { a -> a.get().rank } + 1
  }

  companion object {
    val STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
      { s -> ASPECT[ResourceLocation.parse(s)]!! },
      { a -> ASPECT.getKey(a).toString() }
    )
    val CODEC = ASPECT.byNameCodec()
  }

  fun defaultStack() = AspectStack.of(this, 1)
}
