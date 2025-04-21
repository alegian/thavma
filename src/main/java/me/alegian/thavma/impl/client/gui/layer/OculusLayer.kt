package me.alegian.thavma.impl.client.gui.layer

import me.alegian.thavma.impl.common.item.OculusItem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
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

    if (level != null && player != null && hitResult?.type == HitResult.Type.BLOCK && hitResult is BlockHitResult) {
      val block = level.getBlockState(hitResult.blockPos).block
      if(player.getItemInHand(InteractionHand.MAIN_HAND).item !is OculusItem) return
      if (!OculusItem.SCANNED.contains(block)) return

      graphics.drawCenteredString(
        mc.font,
        Component.translatable(block.descriptionId),
        width / 2,
        height / 3,
        0xFFFFFF
      )
    }
  }
}