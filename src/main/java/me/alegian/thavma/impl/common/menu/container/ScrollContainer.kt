package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.ScrollSlot

class ScrollContainer<T : Menu>(private val menu: T) : SingleContainer<T>(menu) {
  override fun addSlots() {
    menu.addSlot(ScrollSlot(this, 0, this.menu))
    range.track()
  }
}