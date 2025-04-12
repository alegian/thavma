package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.WandSlot

class WandContainer<T : Menu>(private val menu: T) : SingleContainer<T>(menu) {
  fun contains(required: AspectMap): Boolean {
    return AspectContainer.from(this.getItem(0))?.aspects?.contains(required) == true
  }

  override fun addSlots() {
    menu.addSlot(WandSlot(this, 0, this.menu))
    range.track()
  }
}
