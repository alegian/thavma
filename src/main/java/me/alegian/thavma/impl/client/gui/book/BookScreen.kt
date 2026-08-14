package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.ClientHelper
import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.ResearchCategories
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class BookScreen : Screen(Component.literal("book")) {
  companion object {
    private val cornerHeight = FrameRenderable.CORNER_TEXTURE.height
    private val selectorGap = TabSelectorWidget.TEXTURE.height / 2
  }

  private var isScrolling = false
  private var currentCategory: ResearchCategory? = null
  private val tabs = mutableMapOf<ResearchCategory, TabRenderable>()
  val currentTab get() = tabs[currentCategory] ?: tabs.toList().first().second
  private var selectorOffset = 0
  private val entryWidgets = mutableListOf<EntryWidget>()

  private val entries = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.holders()?.toList()
  private var currentEntries = entries?.filter { it.value().category.value() == currentCategory }
  var currentWidgets = listOf<EntryWidget>()


  override fun init() {
    super.init()
    val player = ClientHelper.player() ?: return

    entryWidgets.clear()
    selectorOffset = cornerHeight + selectorGap

    val categoryRegistry = clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)
    currentCategory = categoryRegistry?.getOrThrow(ResearchCategories.THAVMA)

    categoryRegistry?.forEach { category ->
      tabs[category] = addRenderableOnly(
        TabRenderable(
          this,
          category,
          entries?.filter { it.value().category.value() == category },
          player
        )
      )

    }
    updateEntryWidgets()

    currentWidgets = entryWidgets.filter { entryWidget ->
      currentEntries?.map { it.value() }?.contains(entryWidget.entry.value()) == true
    }

    addRenderableOnly(FrameRenderable)
    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)
      ?.sortedBy { it.index }
      ?.forEach { addSelectorWidget(it) }
  }

  private fun addSelectorWidget(category: ResearchCategory) {
    addRenderableWidget(TabSelectorWidget(0, selectorOffset, category) {
      currentCategory = category
      updateEntryWidgets()
    })
    selectorOffset += TabSelectorWidget.TEXTURE.height + selectorGap
  }

  private fun updateEntryWidgets() {
    entryWidgets.forEach {
      val enabled = it.tab == currentTab
      it.visible = enabled
      it.active = enabled
    }
  }

  override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {

    val dimensionX = currentWidgets.maxOf { it.x } - currentWidgets.minOf { it.x }
    val dimensionY = currentWidgets.maxOf { it.y } - currentWidgets.minOf { it.y }

    if (button != 0) {
      this.isScrolling = false
      return false
    } else {
      if (!this.isScrolling) {
        this.isScrolling = true
      } else {
        currentTab.drag(dragX, dragY)
      }

      return true
    }
  }

  override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
    currentTab.zoom(scrollY)
    return true
  }

  override fun isPauseScreen() = false
}

