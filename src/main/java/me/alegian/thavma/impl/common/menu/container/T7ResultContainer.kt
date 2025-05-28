package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.SlotRange
import me.alegian.thavma.impl.common.menu.slot.T7ResultSlot
import net.minecraft.world.inventory.ResultContainer

abstract class T7ResultContainer<T : Menu>(protected val menu: T) : ResultContainer(), T7Container {
  override val range = SlotRange.Single(menu)

  fun getSlot(index: Int): T7ResultSlot<*> {
    val slot = menu.getSlot(this.range.start + index)
    if (slot is T7ResultSlot<*>) return slot
    throw IllegalArgumentException("Thavma Exception: T7ResultContainer contains a slot which is not a T7ResultSlot")
  }

  fun setSlotEnabled(index: Int, enabled: Boolean) {
    getSlot(index).mayPickup = enabled
  }
}
