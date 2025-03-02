package me.alegian.thavma.impl.client.gui.tooltip;

import com.google.common.collect.ImmutableList;
import me.alegian.thavma.impl.client.T7GuiGraphics;
import me.alegian.thavma.impl.client.renderer.AspectRenderer;
import me.alegian.thavma.impl.common.aspect.AspectStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AspectClientTooltipComponent implements ClientTooltipComponent {
  private final ImmutableList<AspectStack> displayedAspects;
  private static final int WIDTH = AspectRenderer.PIXEL_RESOLUTION;
  private static final int PADDING = 3;

  public AspectClientTooltipComponent(AspectTooltipComponent tooltip) {
    displayedAspects = tooltip.getAspectMap().displayedAspects();
  }

  @Override
  public int getHeight() {
    if (isEmpty()) return 0;
    return WIDTH + PADDING * 2;
  }

  @Override
  public int getWidth(Font pFont) {
    if (isEmpty()) return 0;
    return (WIDTH + PADDING) * displayedAspects.size();
  }

  private boolean isEmpty() {
    return displayedAspects.isEmpty();
  }

  @Override
  public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
    if (isEmpty()) return;

    int i = 0;
    for (AspectStack aspectStack : displayedAspects) {
      AspectRenderer.INSTANCE.renderAspect(new T7GuiGraphics(pGuiGraphics), aspectStack, pX + i * (WIDTH + PADDING), pY + PADDING);
      i++;
    }
  }
}
