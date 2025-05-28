package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.SlotRange
import me.alegian.thavma.impl.common.menu.slot.T7Slot
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/**
 * Wrapper around Inventory, that makes it a T7Container
 */
class T7Inventory(private val inventory: Inventory, private val menu: Menu) : T7Container {
  override val range = SlotRange(menu)

  override fun addSlots() {
    range.start()

    for (i in 0..2) {
      for (j in 0..8) menu.addSlot(T7Slot(this, j + i * 9 + 9, menu))
    }

    for (i in 0..8) menu.addSlot(T7Slot(this, i, menu))

    range.end()
  }

  override fun getContainerSize(): Int {
    return this.inventory.containerSize
  }

  override fun isEmpty(): Boolean {
    return this.inventory.isEmpty()
  }

  override fun getItem(slot: Int): ItemStack {
    return this.inventory.getItem(slot)
  }

  override fun removeItem(slot: Int, amount: Int): ItemStack {
    return this.inventory.removeItem(slot, amount)
  }

  override fun removeItemNoUpdate(slot: Int): ItemStack {
    return this.inventory.removeItemNoUpdate(slot)
  }

  override fun setItem(slot: Int, stack: ItemStack) {
    this.inventory.setItem(slot, stack)
  }

  override fun setChanged() {
    this.inventory.setChanged()
  }

  override fun stillValid(player: Player): Boolean {
    return this.inventory.stillValid(player)
  }

  override fun clearContent() {
    this.inventory.clearContent()
  }

  val player: Player
    get() = this.inventory.player
}
