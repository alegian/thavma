package me.alegian.thavma.impl.client.gui.layer

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.util.scaleXY
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.aspect.AspectHelper
import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.item.ArcaneLensItem
import me.alegian.thavma.impl.common.scanning.getScanHitResult
import me.alegian.thavma.impl.common.scanning.hasScanned
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

object ArcaneLensLayer : LayeredDraw.Layer {
  val NO_ASPECTS = "gui.layer." + Thavma.MODID + ".arcane_lens.no_aspects"

  override fun render(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val width = graphics.guiWidth()
    val height = graphics.guiHeight()
    val mc = Minecraft.getInstance()
    val level = mc.level ?: return
    val player = mc.player ?: return

    if (!player.isHolding { stack -> stack.item is ArcaneLensItem }) return

    val hitResult = player.getScanHitResult()

    if (hitResult.type == HitResult.Type.MISS) return

    var text: Component? = null
    var aspects: AspectMap? = null

    when (hitResult) {
      is BlockHitResult -> {
        val blockState = level.getBlockState(hitResult.blockPos)
        val blockAspects = AspectHelper.getAspects(blockState.block)
        text = Component.translatable(blockState.block.descriptionId)
        if (!player.hasScanned(blockState)) {
          if (blockAspects == null || blockAspects.isEmpty)
            text = Component.translatable(NO_ASPECTS)
        } else {
          aspects = blockAspects
        }
      }

      is EntityHitResult -> {
        val entity = hitResult.entity
        val entityAspects = AspectHelper.getAspects(entity)
        text = entity.name
        if (!player.hasScanned(entity)) {
          if (entityAspects == null || entityAspects.isEmpty)
            text = Component.translatable(NO_ASPECTS)
        } else {
          aspects = entityAspects
        }
      }
    }

    if (text == null) return
    graphics.drawCenteredString(
      mc.font,
      text,
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