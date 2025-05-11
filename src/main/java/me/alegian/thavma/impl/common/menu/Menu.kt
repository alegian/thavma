package me.alegian.thavma.impl.common.menu

import me.alegian.thavma.impl.common.menu.container.T7Container
import me.alegian.thavma.impl.common.menu.container.T7Inventory
import me.alegian.thavma.impl.common.menu.slot.SlotRange
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

/**
 * An AbstractContainerMenu that has a player Inventory.
 * Used to dynamically position Slots, and dynamically generate the
 * quick-move operations between all children containers.
 */
abstract class Menu (menuType: MenuType<*>, containerId: Int, playerInventory: Inventory) : AbstractContainerMenu(menuType, containerId), ContainerListener {
  var inventory: T7Inventory
    protected set

  init {
    inventory = T7Inventory(playerInventory, this)
  }

  protected fun moveItemStackToRange(slotItem: ItemStack, range: SlotRange): Boolean {
    return this.moveItemStackTo(slotItem, range.start, range.end + 1, false)
  }

  val player: Player
    get() = inventory.player

  /**
   * slotIndex is relative to this.slots and NOT slot id
   */
  override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
    var originalItem = ItemStack.EMPTY
    val slot = this.slots[slotIndex]
    if (slot.hasItem()) {
      val slotItem = slot.item
      originalItem = slotItem.copy()

      // attempt to move stack, one by one to each target container, until one succeeds
      // zeros are converted to EMPTY. auto-updates dest slot
      val isInventorySlot = this.inventory.range.contains(slotIndex)
      if (isInventorySlot) {
        if (this.quickMovePriorities.stream().noneMatch { container -> this.moveItemStackToRange(slotItem, container.range) }
        ) return ItemStack.EMPTY
      } else if (!this.moveItemStackToRange(slotItem, this.inventory.range)) return ItemStack.EMPTY

      // update source slot, zeros are converted to EMPTY
      if (slotItem.isEmpty) slot.set(ItemStack.EMPTY)
      else slot.setChanged()

      // if nothing was done (there is no space), signal to avoid retrying
      if (slotItem.count == originalItem.count) return ItemStack.EMPTY

      slot.onTake(player, slotItem)
    }

    return originalItem
  }

  protected abstract val quickMovePriorities: List<T7Container>

  override fun dataChanged(menu: AbstractContainerMenu, pDataSlotIndex: Int, pValue: Int) {
  }
}
