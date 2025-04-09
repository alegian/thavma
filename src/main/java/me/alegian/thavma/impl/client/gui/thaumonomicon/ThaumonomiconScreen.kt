package me.alegian.thavma.impl.client.gui.thaumonomicon

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.common.research.ResearchCategory
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import me.alegian.thavma.impl.init.registries.deferred.ResearchCategories
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import kotlin.jvm.optionals.getOrNull

class ThaumonomiconScreen : Screen(Component.literal("Thaumonomicon")) {
  private var isScrolling = false
  var currentCategory = ResearchCategories.TEST_CATEGORY
  private val tabs = mutableMapOf<ResourceKey<ResearchCategory>, TabRenderable>()
  private val currentTab get() = tabs[currentCategory]

  override fun init() {
    super.init()
    this.addRenderableOnly(frame)

    val registryAccess = Minecraft.getInstance().connection?.registryAccess()
    registryAccess?.registry(T7DatapackRegistries.RESEARCH_CATEGORY)?.getOrNull()?.entrySet()?.map { it.key }?.forEach {
      tabs[it] = addRenderableOnly(TabRenderable(this, it))
    }
    registryAccess?.registry(T7DatapackRegistries.RESEARCH_ENTRY)?.getOrNull()?.entrySet()?.map { it.value }?.also {
      for ((tabCategory, tab) in tabs.entries)
        tab.setEntries(it.filter { e -> e.category.compareTo(tabCategory) == 0 })
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

