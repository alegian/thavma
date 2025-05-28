package me.alegian.thavma.impl.client.renderer

import me.alegian.thavma.impl.common.item.HammerItem
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.client.ClientHooks
import net.neoforged.neoforge.client.event.RenderHighlightEvent

object HammerHighlightRenderer {
  /**
   * Used in highlight event.
   * Render outline for the block positions supplied by a hammer.
   * Calls recursive highlight events for each one
   */
  fun render(event: RenderHighlightEvent.Block, hammer: HammerItem, player: LocalPlayer, level: ClientLevel, itemStack: ItemStack, hitResult: BlockHitResult) {
    val levelRenderer = event.levelRenderer
    val camera = event.camera

    for (blockPos in hammer.getValid3x3PositionsExceptOrigin(event.target, level, itemStack, player)) {
      val currHitResult = BlockHitResult(hitResult.getLocation(), hitResult.direction, blockPos, hitResult.isInside)
      if (!ClientHooks.onDrawHighlight(levelRenderer, camera, currHitResult, event.deltaTracker, event.poseStack, event.multiBufferSource)) levelRenderer.renderHitOutline(
        event.poseStack,
        event.multiBufferSource.getBuffer(RenderType.LINES),
        player,
        camera.position.x,
        camera.position.y,
        camera.position.z,
        blockPos,
        level.getBlockState(blockPos)
      )
    }
  }
}
