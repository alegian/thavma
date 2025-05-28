package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import me.alegian.thavma.impl.common.menu.slot.WorkbenchResultSlot

class WorkbenchResultContainer(menu: WorkbenchMenu)
  : T7ResultContainer<WorkbenchMenu>(menu) {
  override fun addSlots() {
    menu.addSlot(WorkbenchResultSlot(this.menu, 0))
    range.track()
  }
}
