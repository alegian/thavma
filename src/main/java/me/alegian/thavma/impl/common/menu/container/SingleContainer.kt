package me.alegian.thavma.impl.common.menu.container

import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.SlotRange
import net.minecraft.core.NonNullList
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/**
 * Convenience implementation for single-slot T7Containers
 */
abstract class SingleContainer<T : Menu>(private val menu: T): T7Container {
  private val itemStacks = NonNullList.withSize(1, ItemStack.EMPTY)
  override val range = SlotRange.Single(menu)

  override fun getContainerSize(): Int {
    return 1
  }

  override fun isEmpty(): Boolean {
    for (itemStack in this.itemStacks) if (!itemStack.isEmpty) return false

    return true
  }

  override fun getItem(pSlot: Int): ItemStack {
    return itemStacks[0]
  }

  override fun removeItem(pSlot: Int, pAmount: Int): ItemStack {
    return ContainerHelper.takeItem(this.itemStacks, 0)
  }

  override fun removeItemNoUpdate(pSlot: Int): ItemStack {
    return ContainerHelper.takeItem(this.itemStacks, 0)
  }

  override fun setItem(pSlot: Int, pStack: ItemStack) {
    itemStacks[0] = pStack
    menu.slotsChanged(this)
  }

  override fun setChanged() {
  }

  override fun stillValid(pPlayer: Player): Boolean {
    return true
  }

  override fun clearContent() {
    itemStacks.clear()
  }
}