package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.common.item.WandItem
import me.alegian.thavma.impl.common.menu.Menu
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class WandSlot<T : Menu>(container: Container, id: Int, menu: T) : T7Slot<T>(container, id, menu) {
  override fun mayPlace(pStack: ItemStack): Boolean {
    return pStack.item is WandItem
  }
}
