package me.alegian.thavma.impl.client.gui.thaumonomicon

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

class ThaumonomiconScreen : Screen(Component.literal("Thaumonomicon")) {
  private var isScrolling = false
  var currentCategory = ResearchCategories.TEST_CATEGORY
  private val tabs = mutableMapOf<ResourceKey<ResearchCategory>, TabRenderable>()
  private val currentTab get() = tabs[currentCategory]
  private var selectorOffset = cornerHeight + selectorGap

  override fun init() {
    super.init()

    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)?.entrySet()?.forEach { (key, category) ->
      tabs[key] = addRenderableOnly(TabRenderable(this, key))
    }
    clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.also {
      for ((tabCategory, tab) in tabs.entries)
        tab.setEntries(it.filter { e -> e.category.compareTo(tabCategory) == 0 })
    }

    addRenderableOnly(frame)
    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)?.forEach { addSelectorWidget(it) }
  }

  private fun addSelectorWidget(category: ResearchCategory) {
    addRenderableWidget(TabSelectorWidget(0, selectorOffset, category) {
      clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)?.getResourceKey(category)?.getOrNull()?.let {
        currentCategory = it
      }
    })
    selectorOffset += TabSelectorWidget.TEXTURE.height + selectorGap
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

