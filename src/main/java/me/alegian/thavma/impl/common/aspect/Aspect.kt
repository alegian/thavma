package me.alegian.thavma.impl.common.aspect

import me.alegian.thavma.impl.init.registries.T7DataMaps
import me.alegian.thavma.impl.init.registries.T7Registries
import net.minecraft.Util
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation

class Aspect(var id: String, var color: Int, val latent: Boolean, val components: Set<Aspect>? = null) {
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

  val opposite: Aspect? by lazy {
    val opposites = T7Registries.ASPECT.getDataMap(T7DataMaps.ASPECT_OPPOSITES)
    opposites[resourceKey]
      ?: opposites.entries.firstOrNull { it.value == this }?.let { T7Registries.ASPECT[it.key] }
  }

  val inversion: Aspect? by lazy {
    val components = components ?: return@lazy opposite
    val invertedComponents = components.map { it.inversion ?: return@lazy null }.toSet()
    T7Registries.ASPECT.firstOrNull { it.components == invertedComponents }
  }

  val isPrimal
    get() = components == null
}

fun Holder<Aspect>.relatedTo(other: Holder<Aspect>) =
  this.value().components.orEmpty().contains(other.value()) ||
    other.value().components.orEmpty().contains(this.value())
