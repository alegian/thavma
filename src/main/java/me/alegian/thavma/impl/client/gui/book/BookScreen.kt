package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.entity.knowsResearch
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.ResearchCategories
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries
import me.alegian.thavma.impl.init.registries.deferred.ResearchEntries.CATEGORIES
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import kotlin.streams.toList

class BookScreen : Screen(Component.literal("book")) {
  companion object {
    private val cornerHeight = FrameRenderable.CORNER_TEXTURE.height
    private val selectorGap = TabSelectorWidget.TEXTURE.height / 2
  }

  private var isScrolling = false
  private var currentCategory: ResearchCategory? = null
  private val tabs = mutableMapOf<ResearchCategory, TabRenderable>()
  private val backgrounds = mutableListOf<TabRenderable>()
  val currentTab get() = tabs[currentCategory] ?: tabs.toList().first().second
  private var selectorOffset = 0
  private val entryWidgets = mutableListOf<EntryWidget>()

  private val entries = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.holders()?.toList()
  val haha = entries.apply { println(this) }
  private var currentEntries = entries?.filter { it.value().category.value() == currentCategory }
    //actualEntries[currentCategory] ?: actualEntries.toList().first().second
  private val hehe = currentEntries.apply { println(this) }
  var relevance = entryWidgets.filter { entryWidget -> currentEntries?.map{ it.value() }?.contains(entryWidget.entry.value()) == true  }

  override fun init() {
    super.init()
    val player = Minecraft.getInstance().player ?: return

    println("the list of actual entries is")
    println(entries)
    println("the current category is $currentCategory")


    entryWidgets.clear()
    selectorOffset = cornerHeight + selectorGap

    val categoryRegistry = clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)
    currentCategory = categoryRegistry?.getOrThrow(ResearchCategories.STORY)
    currentEntries = entries?.filter { it.value().category.value() == currentCategory }

    println("and current entries are")
    println(currentEntries)
    categoryRegistry?.forEach { category ->
      tabs[category] = addRenderableOnly(TabRenderable(this, category, entries?.filter { it.value().category.value() == category }, player))
    }
//    clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.holders()?.forEach {
//      val tab = tabs[it.value().category.value()]
//      var shown = player.knowsResearch(it)
//      for (p in it.value().parents(player.level()))
//        if (player.knowsResearch(p)) shown = true
//
//      if (tab != null && shown)
//        entryWidgets.add(addRenderableWidget(EntryWidget(this, tab, it)))
//    }

    // moved EntryWidget creation logic into TabRenderable.kt, here only adding them via
    // the protected method addRenderableWidget
    tabs.forEach { tab -> entryWidgets.addAll(tab.value.entryWidgets.map { addRenderableWidget(it) }) }
    println("And the entry widgets added are")
    println(entryWidgets)
    updateEntryWidgets()

    relevance = entryWidgets.filter { entryWidget -> currentEntries?.map{ it.value() }?.contains(entryWidget.entry.value()) == true  }

    addRenderableOnly(FrameRenderable)
    clientRegistry(T7DatapackRegistries.RESEARCH_CATEGORY)
      ?.sortedBy { it.index }
      ?.forEach { addSelectorWidget(it) }
  }

  private fun addSelectorWidget(category: ResearchCategory) {
    addRenderableWidget(TabSelectorWidget(0, selectorOffset, category) {
      currentCategory = category
      currentEntries = entries?.filter { it.value().category.value() == currentCategory }
      relevance = entryWidgets.filter { entryWidget -> currentEntries?.map{ it.value() }?.contains(entryWidget.entry.value()) == true  }
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

    val dimensionX = relevance.maxOf { it.x } - relevance.minOf { it.x }
    val dimensionY = relevance.maxOf { it.y } - relevance.minOf { it.y }

    //println(dimensionX)
    //println(dimensionY)

    if (button != 0) {
      this.isScrolling = false
      return false
    } else {
      if (!this.isScrolling) {
        this.isScrolling = true
      } else {
        currentTab.drag(dragX/(dimensionX-1), dragY/(dimensionY-1))
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

