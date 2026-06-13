package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.ANIMATION_SPEED
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FADE_IN_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FusedSymbol
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.MARGIN_CONST
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationStart
import me.alegian.thavma.impl.client.util.blit
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.math.pow

@OnlyIn(Dist.CLIENT)
object NotifAnimationLayer : LayeredDraw.Layer {

  val mc = Minecraft.getInstance()
  val scaledWidth = mc.window.guiScaledWidth
  val scaledHeight = mc.window.guiScaledHeight

  override fun render(
    graphics: GuiGraphics,
    deltaTracker: DeltaTracker
  ) {
    val player = mc.player ?: return
    val currentTime = player.level().gameTime
    val dexponential = 0.95.pow(
      1 / (1.001 - ((currentTime - animationStart) / (FADE_IN_PRIO * (1 - 1 / ANIMATION_SPEED))).coerceIn(
        0.0,
        1.0
      ))
    ).toFloat()

    //if (PriorityNotifLayer.shouldPlayIntro) {

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()

// If usePose automatically pushes and pops, keep it.
// Otherwise, explicitly push and pop standard Minecraft way:
    graphics.pose().pushPose()

// 1. Calculate your target screen coordinates
    val xPos = scaledWidth / 2f + 100
    val yPos = scaledHeight - (scaledHeight / MARGIN_CONST) - 3f + 50

// 2. Translate to the target coordinate FIRST, and push it forward on the Z-axis (Render Above All)
    graphics.pose().translate(xPos, yPos, 1f)

// 3. Apply your scale
    graphics.pose().scale(1 / 14f, 0.0625f, 1f)

// 4. Blit the texture offset by half its width/height so it centers properly
    graphics.blit(FusedSymbol)
    graphics.blit(
      ResourceLocation.fromNamespaceAndPath(Thavma.MODID, "textures/layer/symbol_fused.png"),
      0,
      0,
      0,
      0,
      256,
      256
    )

    graphics.pose().popPose() // ONLY do this if you used pushPose(). Remove if usePose() does this for you!

    RenderSystem.disableBlend()


//      RenderSystem.enableBlend()
//      RenderSystem.defaultBlendFunc()
//      RenderSystem.setShaderColor(1f, 1f, 1f, animationOpacity)
//
//      if (animationOpacity < 1f) {
//        graphics.pose().pushPose()
//        graphics.pose().scale(2f, 2f, 1f)
//        graphics.blit(
//          FusedSymbol.location,
//          scaledWidth / 2 - 4,
//          (scaledHeight - scaledHeight / 2.4f - 3).toInt(),
//          0,
//          0,
//          8,
//          7
//        )
//        graphics.pose().popPose()
//      } else {
//        RenderSystem.setShaderColor(1f, 1f, 1f, dexponential)
//        graphics.pose().pushPose()
//        graphics.pose().scale(2f, 2f, 1f)
//        graphics.blit(
//          Asymbol.location,
//          (scaledWidth / 2 - (scaledWidth * 2 / 6)*(1-dexponential) - 4).toInt(),
//          (scaledHeight - (scaledHeight / 2.4f)*(1-dexponential) - 3).toInt(),
//          0,
//          0,
//          8,
//          7
//        )
//        graphics.blit(
//          Osymbol.location,
//          (scaledWidth / 2 + (scaledWidth * 2 / 6)*(1-dexponential) - 4).toInt(),
//          (scaledHeight - (scaledHeight / 2.4f)*(1-dexponential) - 3).toInt(),
//          0,
//          0,
//          8,
//          7
//        )
//        graphics.pose().popPose()
//      }
//
//      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
//      RenderSystem.disableBlend()
  }
}
//}