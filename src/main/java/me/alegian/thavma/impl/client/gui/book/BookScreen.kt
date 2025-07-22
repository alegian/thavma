package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.entity.knowsResearch
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.ResearchCategories
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import kotlin.jvm.optionals.getOrNull

private val cornerHeight = FrameRenderable.CORNER_TEXTURE.height
private val selectorGap = TabSelectorWidget.TEXTURE.height / 2

class BookScreen : Screen(Component.literal("Thaumonomicon")) {
  private var isScrolling = false
  var currentCategory = ResearchCategories.THAVMA
  private val tabs = mutableMapOf<ResourceKey<ResearchCategory>, TabRenderable>()
  private val currentTab get() = tabs[currentCategory]
  private var selectorOffset = 0
  private val entryWidgets = mutableListOf<EntryWidget>()

  override fun init() {
    super.init()
    val player = Minecraft.getInstance().player ?: return

    entryWidgets.clear()
    selectorOffset = cornerHeight + selectorGap

    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)?.registryKeySet()?.forEach {
      tabs[it] = addRenderableOnly(TabRenderable(this, it))
    }
    clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.entrySet()?.forEach { (key, entry) ->
      val tab = tabs[entry.category]
      var shown = player.knowsResearch(key)
      for (p in entry.parents(player.level()))
        if (player.knowsResearch(p)) shown = true

      if (tab != null && shown)
        entryWidgets.add(addRenderableWidget(EntryWidget.of(this, tab, entry)))
    }
    updateEntryWidgets()

    addRenderableOnly(FrameRenderable)
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
        currentTab?.drag(dragX, dragY)
      }

      return true
    }
  }

  override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
    currentTab?.zoom(scrollY)
    return true
  }

  override fun isPauseScreen() = false
}

