package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.SlotRange
import me.alegian.thavma.impl.common.menu.slot.T7Slot
import net.minecraft.world.inventory.TransientCraftingContainer

class CraftingContainer3x3(private val menu: Menu) : TransientCraftingContainer(menu, 3, 3), T7Container {
  override val range = SlotRange(menu)

  override fun addSlots() {
    range.start()
    for (i in 0..2) {
      for (j in 0..2) this.menu.addSlot(T7Slot(this, j + i * 3, menu))
    }
    range.end()
  }
}
