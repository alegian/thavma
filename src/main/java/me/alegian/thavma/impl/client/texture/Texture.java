package me.alegian.thavma.impl.client.texture;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static me.alegian.thavma.impl.ThavmaKt.rl;

/**
 * Width and height refer to the useful part of the texture, not its entirety.
 * In practice many textures (like inventories) have huge empty spaces we don't care about;
 */
@OnlyIn(Dist.CLIENT)
public record Texture(ResourceLocation location, int width, int height, int canvasWidth, int canvasHeight) {
  public Texture(String path, int width, int height) {
    this(rl("textures/" + path + ".png"), width, height, width, height);
  }

  public Texture(String path, int width, int height, int canvasWidth, int canvasHeight) {
    this(rl("textures/" + path + ".png"), width, height, canvasWidth, canvasHeight);
  }
}
