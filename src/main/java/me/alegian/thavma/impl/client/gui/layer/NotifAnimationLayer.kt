package me.alegian.thavma.impl.client.gui.layer

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FADE_IN_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.FusedSymbol
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.STATIC_DELAY_PRIO
import me.alegian.thavma.impl.client.gui.layer.PriorityNotifLayer.animationOpacity
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
  val timeLimit = FADE_IN_PRIO + STATIC_DELAY_PRIO

  override fun render(
    graphics: GuiGraphics,
    deltaTracker: DeltaTracker
  ) {
    val player = mc.player ?: return
    val currentTime = player.level().gameTime
//    val dexponential = 0.95.pow(
//      1 / (1.001 - ((currentTime - animationStart) / (timeLimit * (1 - 1 / ANIMATION_SPEED))).coerceIn(
//        0.0F,
//        1.0F
//      ))
//    ).toFloat()                                           * (1 - 1.0 / ANIMATION_SPEED)
    val dexponential = ((currentTime - animationStart) / (timeLimit * 2 / 3f)).coerceIn(0f, 1f)


    if (PriorityNotifLayer.shouldPlayIntro) {
      if (animationOpacity < 1f) {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(1f, 1f, 1f, animationOpacity)
        graphics.pose().pushPose()
        val xPos = 230f
        val yPos = 160f
        graphics.pose().translate(xPos, yPos, 1f)
        graphics.pose().scale(1 / 14f, 0.0625f, 1f)
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
        graphics.pose().popPose()
        RenderSystem.disableBlend()
      }

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
//      }
      else {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1 / 0.85.pow(5).toFloat())
        graphics.pose().pushPose()
        val xPos = 230f
        val yPos = 160f
        graphics.pose().translate(xPos, yPos, 1f)
        graphics.pose().scale(1 / 14f, 0.0625f, 1f)
        graphics.blit(
          ResourceLocation.fromNamespaceAndPath(Thavma.MODID, "textures/layer/symbol_alpha.png"),
          (xPos + 0.5.pow(-dexponential.toDouble().pow(-5)).toInt()).toInt(),
          0,
          0,
          0,
          256,
          256
        )
        graphics.blit(
          ResourceLocation.fromNamespaceAndPath(Thavma.MODID, "textures/layer/symbol_omega.png"),
          (+(2300) * ((1 - (1 - dexponential)))).toInt(),
          0,
          0,
          0,
          256,
          256
        )
        graphics.pose().popPose()
      }

      RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
      RenderSystem.disableBlend()
  }
  }
}
//}