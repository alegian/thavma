package me.alegian.thavma.impl.client.texture.atlas

import me.alegian.thavma.impl.rl
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.TextureAtlasHolder
import net.minecraft.resources.ResourceLocation

private const val KEY: String = "aspects"
private val LOCATION: ResourceLocation = rl("atlas/$KEY")

object AspectAtlas : TextureAtlasHolder(Minecraft.getInstance().textureManager, LOCATION, rl(KEY)) {
  public override fun getSprite(pLocation: ResourceLocation): TextureAtlasSprite {
    return super.getSprite(pLocation)
  }
}
