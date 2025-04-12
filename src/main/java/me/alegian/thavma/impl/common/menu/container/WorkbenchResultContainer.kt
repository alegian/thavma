package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import me.alegian.thavma.impl.common.menu.slot.WorkbenchResultSlot

class WorkbenchResultContainer(private val menu: WorkbenchMenu)
  : T7ResultContainer<WorkbenchMenu>(menu, menu.craftingContainer) {
  override fun addSlots() {
    menu.addSlot(WorkbenchResultSlot(this.menu, 0))
    range.track()
  }
}
