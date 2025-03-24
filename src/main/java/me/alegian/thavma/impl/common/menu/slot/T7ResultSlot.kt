package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.common.menu.Menu
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.inventory.ResultContainer
import net.minecraft.world.inventory.ResultSlot
import net.minecraft.world.item.ItemStack

/**
 * A Sized ResultSlot, with a back-reference to a Menu
 */
open class T7ResultSlot<T : Menu>(override val menu: T, craftingContainer: CraftingContainer, container: ResultContainer, id: Int) : ResultSlot(menu.player, craftingContainer, container, id, 0, 0), DynamicSlot<T> {
  override var size = 0
  var mayPickup = true
  override var actualX = 0f
  override var actualY = 0f

  override fun onTake(player: Player, stack: ItemStack) {}

  override fun mayPickup(player: Player): Boolean {
    return this.mayPickup
  }
}
