package me.alegian.thavma.impl.client.event

import com.mojang.datafixers.util.Either
import me.alegian.thavma.impl.client.T7KeyMappings
import me.alegian.thavma.impl.client.clientPlayerHasRevealing
import me.alegian.thavma.impl.client.clientPlayerHoldingWand
import me.alegian.thavma.impl.client.getClientPlayerEquipmentItem
import me.alegian.thavma.impl.client.gui.foci.FociScreen
import me.alegian.thavma.impl.client.gui.tooltip.AspectTooltipComponent
import me.alegian.thavma.impl.client.gui.tooltip.containedPrimalsComponent
import me.alegian.thavma.impl.client.renderer.AspectRenderer
import me.alegian.thavma.impl.client.renderer.HammerHighlightRenderer
import me.alegian.thavma.impl.common.block.AuraNodeBlock
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.entity.hasScanned
import me.alegian.thavma.impl.common.entity.setKnowledge
import me.alegian.thavma.impl.common.entity.tryScan
import me.alegian.thavma.impl.common.item.HammerItem
import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.api.distmarker.Dist
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.event.RenderTooltipEvent.GatherComponents
import thedarkcolour.kotlinforforge.neoforge.forge.DIST
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS as KFF_GAME_BUS


private var allowHammerOutlineEvents = true

private fun renderBlockHighlight(event: RenderHighlightEvent.Block) {
  val level = Minecraft.getInstance().level ?: return
  val hitResult = event.target
  if (hitResult.type == HitResult.Type.MISS) return
  val targetPos = hitResult.blockPos
  val player = Minecraft.getInstance().player ?: return
  val itemStack = player.mainHandItem
  val item = itemStack.item

  if (allowHammerOutlineEvents) if (item is HammerItem) {
    allowHammerOutlineEvents = false
    HammerHighlightRenderer.render(event, item, player, level, itemStack, hitResult)
    allowHammerOutlineEvents = true
  }

  // aura nodes have no outline
  if (level.getBlockState(targetPos).block is AuraNodeBlock) event.isCanceled = true
}

private fun renderLevelAfterWeather(event: RenderLevelStageEvent) {
  if (event.stage !== RenderLevelStageEvent.Stage.AFTER_WEATHER) return

  // general purpose useful stuff
  val minecraft = Minecraft.getInstance()
  val level = minecraft.level ?: return
  val hitResult = minecraft.hitResult
  if (hitResult == null || hitResult.type != HitResult.Type.BLOCK) return
  val blockPos = (hitResult as BlockHitResult).blockPos

  // aspect renderer
  if (!AspectContainer.isAspectContainer(level, blockPos)) return
  if (!clientPlayerHasRevealing()) return

  AspectContainer.at(level, blockPos)?.aspects?.let {
    AspectRenderer.renderAfterWeather(
      it,
      event.poseStack,
      event.camera,
      blockPos
    )
  }
}

private fun gatherTooltipComponents(event: GatherComponents) {
  if (!clientPlayerHasRevealing() || event.itemStack.isEmpty) return

  AspectContainer.from(event.itemStack)?.aspects?.let {
    event.tooltipElements.add(
      Either.left(
        containedPrimalsComponent(it)
      )
    )
  }

  if (!Screen.hasShiftDown()) return
  val player = Minecraft.getInstance().player ?: return
  if (!player.isCreative && !player.hasScanned(event.itemStack)) return

  event.tooltipElements.addLast(Either.right(AspectTooltipComponent(event.itemStack)))
}

private fun renderPlayerPre(event: RenderPlayerEvent.Pre) {
  val model = event.renderer.model

  // if chestplate exists, disable sleeves & jacket to prevent clipping with thin armors
  getClientPlayerEquipmentItem(EquipmentSlot.CHEST)?.let {
    model.leftSleeve.visible = false
    model.rightSleeve.visible = false
    model.jacket.visible = false
  }
  // if leggings exist, disable pants to prevent clipping with thin armors
  getClientPlayerEquipmentItem(EquipmentSlot.LEGS)?.let {
    model.leftPants.visible = false
    model.rightPants.visible = false
  }
}

private fun playerClone(event: ClientPlayerNetworkEvent.Clone) {
  val oldScans = event.oldPlayer.getData(T7Attachments.SCANNED).scanned.toList()
  event.newPlayer.tryScan(oldScans)
  val oldKnowledge = event.oldPlayer.getData(T7Attachments.KNOWLEDGE).knowledge.toList()
  event.newPlayer.setKnowledge(oldKnowledge)
}

private fun clientTick(event: ClientTickEvent.Post) {
  val mc = Minecraft.getInstance()
  if (T7KeyMappings.FOCI.isDown && mc.screen == null && mc.overlay == null && clientPlayerHoldingWand())
    mc.setScreen(FociScreen())
}

fun registerClientGameEvents() {
  if (DIST != Dist.CLIENT) return

  KFF_GAME_BUS.addListener(::renderBlockHighlight)
  KFF_GAME_BUS.addListener(::renderLevelAfterWeather)
  KFF_GAME_BUS.addListener(::gatherTooltipComponents)
  KFF_GAME_BUS.addListener(::renderPlayerPre)
  KFF_GAME_BUS.addListener(::playerClone)
  KFF_GAME_BUS.addListener(::clientTick)
}