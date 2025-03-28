package me.alegian.thavma.impl.client.gui.thaumonomicon

import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.Tab
import me.alegian.thavma.impl.client.gui.thaumonomicon.widget.frame
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class ThaumonomiconScreen : Screen(Component.literal("Thaumonomicon")) {
  private val tab = Tab(300f, 300f)
  private var isScrolling = false

  override fun init() {
    super.init()
    this.addRenderableOnly(tab)
    this.addRenderableOnly(frame)
  }

  override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, dragX: Double, dragY: Double): Boolean {
    if (button != 0) {
      this.isScrolling = false
      return false
    } else {
      if (!this.isScrolling) {
        this.isScrolling = true
      } else {
        tab.handleScroll(dragX.toFloat().toDouble(), dragY.toFloat().toDouble())
      }

      return true
    }
  }

  override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollX: Double, scrollY: Double): Boolean {
    tab.zoom(scrollY.toFloat())
    return true
  }
}

