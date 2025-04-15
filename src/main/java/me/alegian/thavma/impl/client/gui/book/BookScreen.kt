package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.client.texture.T7Textures
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.ResearchCategories
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import kotlin.jvm.optionals.getOrNull

private val cornerHeight = T7Textures.Thaumonomicon.FRAME_CORNER.height
private val selectorGap = TabSelectorWidget.TEXTURE.height / 2

class BookScreen : Screen(Component.literal("Thaumonomicon")) {
  private var isScrolling = false
  var currentCategory = ResearchCategories.TEST_CATEGORY
  private val tabs = mutableMapOf<ResourceKey<ResearchCategory>, TabRenderable>()
  private val currentTab get() = tabs[currentCategory]
  private var selectorOffset = 0
  private val entryWidgets = mutableListOf<EntryWidget>()

  override fun init() {
    super.init()

    entryWidgets.clear()
    selectorOffset = cornerHeight + selectorGap

    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)?.entrySet()?.forEach { (key) ->
      tabs[key] = addRenderableOnly(TabRenderable(this, key))
    }
    clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.forEach {
      val tab = tabs[it.category]
      if (tab != null) entryWidgets.add(addRenderableWidget(EntryWidget.of(this, tab, it)))
    }
    updateEntryWidgets()

    addRenderableOnly(frame)
    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)
      ?.sortedBy { it.index }
      ?.forEach { addSelectorWidget(it) }
  }

  private fun addSelectorWidget(category: ResearchCategory) {
    addRenderableWidget(TabSelectorWidget(0, selectorOffset, category) {
      clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)?.getResourceKey(category)?.getOrNull()?.let {
        currentCategory = it
        updateEntryWidgets()
      }
    })
    selectorOffset += TabSelectorWidget.TEXTURE.height + selectorGap
  }

  private fun updateEntryWidgets() {
    entryWidgets.forEach {
      val enabled = it.tab.category == currentCategory
      it.visible = enabled
      it.active = enabled
    }
  }

  override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
    if (button != 0) {
      this.isScrolling = false
      return false
    } else {
      if (!this.isScrolling) {
        this.isScrolling = true
      } else {
        currentTab?.drag(dragX.toDouble(), dragY.toDouble())
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
    currentTab?.zoom(scrollY)
    return true
  }
}

