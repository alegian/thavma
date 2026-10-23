package me.alegian.thavma.impl.client.texture

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec2

/**
 * Width and height refer to the useful part of the texture, not its entirety.
 * In practice many textures (like inventories) have huge empty spaces we don't care about
 */
data class Texture(
  val location: ResourceLocation,
  val width: Int,
  val height: Int,
  val canvasWidth: Int,
  val canvasHeight: Int
) {
  constructor(path: String, width: Int, height: Int) : this(rl("textures/$path.png"), width, height, width, height)

  constructor(path: String, width: Int, height: Int, canvasWidth: Int, canvasHeight: Int) : this(rl("textures/$path.png"), width, height, canvasWidth, canvasHeight)

  val size: Vec2
    get() = Vec2(width.toFloat(), height.toFloat())

  companion object {
    val CODEC: Codec<Texture> = RecordCodecBuilder.create { instance ->
      instance.group(
        ResourceLocation.CODEC.fieldOf("location").forGetter(Texture::location),
        Codec.INT.fieldOf("width").forGetter(Texture::width),
        Codec.INT.fieldOf("height").forGetter(Texture::height),
        Codec.INT.fieldOf("canvas_width").forGetter(Texture::canvasWidth),
        Codec.INT.fieldOf("canvas_height").forGetter(Texture::canvasHeight)
      ).apply(instance, ::Texture)
    }
  }
}
