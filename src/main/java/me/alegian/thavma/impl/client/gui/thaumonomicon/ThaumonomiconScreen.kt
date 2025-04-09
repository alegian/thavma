package me.alegian.thavma.impl.client.gui.thaumonomicon

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.gui.thaumonomicon.renderable.Tab
import me.alegian.thavma.impl.client.gui.thaumonomicon.renderable.frame
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import kotlin.jvm.optionals.getOrNull

class ThaumonomiconScreen : Screen(Component.literal("Thaumonomicon")) {
  private val tab = Tab(300f, 300f)
  private var isScrolling = false
  private var categories: List<ResearchCategory>? = null
  private var entries: List<ResearchEntry>? = null

  override fun init() {
    super.init()
    this.addRenderableOnly(tab)
    this.addRenderableOnly(frame)

    val registryAccess = Minecraft.getInstance().connection?.registryAccess()
    categories = registryAccess?.registry(T7DatapackRegistries.RESEARCH_CATEGORY)?.getOrNull()?.entrySet()?.map { it.value }
    entries = registryAccess?.registry(T7DatapackRegistries.RESEARCH_ENTRY)?.getOrNull()?.entrySet()?.map { it.value }
  }

  override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
    if (button != 0) {
      this.isScrolling = false
      return false
    } else {
      if (!this.isScrolling) {
        this.isScrolling = true
      } else {
        tab.drag(dragX.toDouble(), dragY.toDouble())
      }

      return true
    }
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    // allows negative size drawing, which greatly simplifies math
    RenderSystem.disableCull()
    super.render(guiGraphics, mouseX, mouseY, partialTick)
    RenderSystem.enableCull()
  }

  override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
    tab.zoom(scrollY.toFloat())
    return true
  }
}

