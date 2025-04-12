package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.RuneSlot

class RuneContainer<T : Menu>(private val menu: T) : SingleContainer<T>(menu) {
  override fun addSlots() {
    menu.addSlot(RuneSlot(this, 0, this.menu))
    range.track()
  }
}