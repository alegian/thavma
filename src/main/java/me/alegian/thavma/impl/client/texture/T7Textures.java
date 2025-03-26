package me.alegian.thavma.impl.client.texture;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class T7Textures {
  public static final class Thaumonomicon {
    private static final String CONTEXT = "gui/thaumonomicon/";

    public static final Texture FRAME = new Texture(CONTEXT + "frame", 1024, 512);
    public static final Texture TAB_BG = new Texture(CONTEXT + "tab_bg", 512, 512);
    public static final Texture NODE = new Texture(CONTEXT + "node", 32, 32);
    public static final Texture ARROW_HEAD = new Texture(CONTEXT + "arrow_head", 32, 32);
    public static final Texture LINE = new Texture(CONTEXT + "line", 32, 32);
    public static final Texture CORNER_1X1 = new Texture(CONTEXT + "corner_1x1", 32, 32);
    public static final Texture CORNER_2X2 = new Texture(CONTEXT + "corner_2x2", 96, 96);
  }
}
