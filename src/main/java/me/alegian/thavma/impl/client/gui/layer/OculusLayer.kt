package me.alegian.thavma.impl.client.gui.layer

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.getAspects
import me.alegian.thavma.impl.common.entity.hasScanned
import me.alegian.thavma.impl.common.item.OculusItem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

object OculusLayer : LayeredDraw.Layer {
  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val width = graphics.guiWidth()
    val height = graphics.guiHeight()
    val mc = Minecraft.getInstance()
    val hitResult = mc.hitResult
    val level = mc.level
    val player = mc.player

    if (
      level == null ||
      player == null ||
      hitResult?.type != HitResult.Type.BLOCK ||
      hitResult !is BlockHitResult ||
      !player.isHolding { stack -> stack.item is OculusItem }
    ) return

    val block = level.getBlockState(hitResult.blockPos).block
    if (!player.hasScanned(block)) return

    graphics.drawCenteredString(
      mc.font,
      Component.translatable(block.descriptionId),
      width / 2,
      3 * height / 8,
      0xFFFFFF
    )

    val aspects = getAspects(block) ?: return

    graphics.usePose {
      translateXY(width / 2, 5 * height / 8)
      scaleXY(2)
      AspectRenderer.renderAspectsWrapped(aspects, graphics)
    }
  }
}