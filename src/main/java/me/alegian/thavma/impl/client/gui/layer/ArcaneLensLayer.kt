package me.alegian.thavma.impl.client.gui.layer

import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.getAspects
import me.alegian.thavma.impl.common.entity.getScanHitResult
import me.alegian.thavma.impl.common.entity.hasScanned
import me.alegian.thavma.impl.common.item.ArcaneLensItem
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

object ArcaneLensLayer : LayeredDraw.Layer {
  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val width = graphics.guiWidth()
    val height = graphics.guiHeight()
    val mc = Minecraft.getInstance()
    val level = mc.level ?: return
    val player = mc.player ?: return

    if (!player.isHolding { stack -> stack.item is ArcaneLensItem }) return

    val hitResult = player.getScanHitResult()

    if (hitResult.type == HitResult.Type.MISS) return

    var displayName: Component? = null
    var aspects: AspectMap? = null

    when (hitResult) {
      is BlockHitResult -> {
        val blockState = level.getBlockState(hitResult.blockPos)
        if (!player.hasScanned(blockState)) return
        displayName = Component.translatable(blockState.block.descriptionId)
        aspects = getAspects(blockState.block)
      }

      is EntityHitResult -> {
        val entity = hitResult.entity
        if (!player.hasScanned(entity)) return
        displayName = entity.name
        aspects = getAspects(entity)
      }
    }

    if (displayName == null) return
    graphics.drawCenteredString(
      mc.font,
      displayName,
      width / 2,
      3 * height / 8,
      0xFFFFFF
    )

    if (aspects == null) return
    graphics.usePose {
      translateXY(width / 2, 5 * height / 8)
      scaleXY(2)
      AspectRenderer.renderAspectsWrapped(aspects, graphics)
    }
  }
}