package me.alegian.thavma.impl.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.alegian.thavma.impl.Thavma;
import me.alegian.thavma.impl.client.T7GuiGraphics;
import me.alegian.thavma.impl.client.texture.atlas.AspectAtlas;
import me.alegian.thavma.impl.client.util.GuiGraphicsExtensionsKt;
import me.alegian.thavma.impl.common.aspect.Aspect;
import me.alegian.thavma.impl.common.aspect.AspectMap;
import me.alegian.thavma.impl.common.aspect.AspectStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector2f;

@OnlyIn(Dist.CLIENT)
public class AspectRenderer {
  public static final int ROW_SIZE = 5;
  public static final float QUAD_SIZE = .3f; // aspect width is .3 blocks
  private static final int PIXEL_RESOLUTION = 16; // pixels per block

  /**
   * Renders the Aspect contents of an AspectContainer, using GuiGraphics.<br>
   * GuiGraphics assumes integer pixel coordinates, so we multiply everything by 16 (so a block is 16x16).
   */
  public static void renderAfterWeather(AspectMap aspects, PoseStack poseStack, Camera camera, BlockPos blockPos) {
    if (aspects.isEmpty()) return;

    poseStack.pushPose();
    var cameraPos = camera.getPosition();
    poseStack.translate(blockPos.getX() - cameraPos.x() + 0.5d, blockPos.getY() - cameraPos.y() + 1.25d + AspectRenderer.QUAD_SIZE / 2, blockPos.getZ() - cameraPos.z() + 0.5d);
    var angle = RenderHelperKt.calculatePlayerAngle(blockPos.getCenter());
    poseStack.mulPose(Axis.YP.rotation(angle));
    poseStack.scale(AspectRenderer.QUAD_SIZE, AspectRenderer.QUAD_SIZE, 1); // this puts us in "aspect space" where 1 means 1 aspect width

    T7GuiGraphics guiGraphics = new T7GuiGraphics(Minecraft.getInstance(), poseStack, Minecraft.getInstance().renderBuffers().bufferSource());

    // these offsets account for wrapping to new lines, and centering the aspects
    Vector2f[] offsets = AspectRenderer.calculateOffsets(aspects.size());

    int i = 0;
    for (AspectStack aspectStack : aspects.displayedAspects()) {
      poseStack.pushPose();
      poseStack.translate(offsets[i].x, offsets[i].y, 0);

      // gui rendering is done in pixel space
      poseStack.scale(1f / AspectRenderer.PIXEL_RESOLUTION, -1f / AspectRenderer.PIXEL_RESOLUTION, 1f);
      AspectRenderer.renderAspect(guiGraphics, aspectStack, -AspectRenderer.PIXEL_RESOLUTION / 2, -AspectRenderer.PIXEL_RESOLUTION / 2);

      poseStack.popPose();
      i++;
    }

    guiGraphics.flush();
    poseStack.popPose();
  }

  public static void renderAspect(T7GuiGraphics guiGraphics, AspectStack aspectStack, int pX, int pY) {
    AspectRenderer.drawAspectIcon(guiGraphics, aspectStack.aspect(), pX, pY);
    AspectRenderer.drawCount(guiGraphics, String.valueOf(aspectStack.amount()), pX, pY);
  }

  public static void drawAspectIcon(T7GuiGraphics guiGraphics, Aspect aspect, int pX, int pY) {
    var sprite = AspectAtlas.sprite(Thavma.INSTANCE.rl(aspect.getId()));

    var color = aspect.getColor();
    RenderSystem.disableDepthTest();
    guiGraphics.blit(
        pX,
        pY,
        0,
        AspectRenderer.PIXEL_RESOLUTION,
        AspectRenderer.PIXEL_RESOLUTION,
        sprite,
        color
    );
    RenderSystem.enableDepthTest();
  }

  public static Vector2f[] calculateOffsets(int numAspects) {
    Vector2f[] offsets = new Vector2f[numAspects];

    int rows = (int) Math.ceil(1f * numAspects / AspectRenderer.ROW_SIZE);
    for (int i = 0; i < rows; i++) {
      int cols = Math.min(numAspects - (i * AspectRenderer.ROW_SIZE), AspectRenderer.ROW_SIZE);
      for (int j = 0; j < cols; j++) {
        float xOffset = (cols - 1) / -2f + j;
        offsets[i * AspectRenderer.ROW_SIZE + j] = new Vector2f(xOffset, i);
      }
    }

    return offsets;
  }

  public static void drawCount(T7GuiGraphics guiGraphics, String text, int pX, int pY) {
    var poseStack = guiGraphics.pose();
    poseStack.pushPose();
    poseStack.translate(pX + AspectRenderer.PIXEL_RESOLUTION, pY + AspectRenderer.PIXEL_RESOLUTION, 0.0001f); // start bottom right, like item count. slightly increase Z to avoid z fighting
    poseStack.scale(0.5F, 0.5F, 0.5F);
    Font font = Minecraft.getInstance().font;
    poseStack.translate(-font.width(text), -font.lineHeight, 0);
    GuiGraphicsExtensionsKt.drawOutlinedSeethroughString(guiGraphics, font, text, 0xFFFFFF, 0);
    poseStack.popPose();
  }

  public static int getPixelResolution() {
    return AspectRenderer.PIXEL_RESOLUTION;
  }
}
