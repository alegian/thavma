package me.alegian.thavma.impl.common.aspect

import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.T7Registries
import net.minecraft.Util
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation

class Aspect(var id: String, var color: Int, val isPrimal: Boolean) {
  val translationId by lazy {
    Util.makeDescriptionId(T7Registries.ASPECT.key().location().path, T7Registries.ASPECT.getKey(this))
  }
  val resourceKey
    get() = T7Registries.ASPECT.getResourceKey(this).get()

  companion object {
    val STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
      { s -> T7Registries.ASPECT[ResourceLocation.parse(s)]!! },
      { a -> T7Registries.ASPECT.getKey(a).toString() }
    )
    val CODEC = T7Registries.ASPECT.byNameCodec()
  }

  fun defaultStack() = AspectStack.of(this, 1)

  fun wrapAsHolder() = T7Registries.ASPECT.wrapAsHolder(this)
}

fun Holder<Aspect>.relatedAspects() =
  getData(T7DataMaps.ASPECT_RELATIONS) ?: listOf()

fun Holder<Aspect>.relatedTo(other: Holder<Aspect>) =
  this.relatedAspects().contains(other.value()) ||
      other.relatedAspects().contains(this.value())

