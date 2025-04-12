package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.init.registries.deferred.T7Items
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class RuneSlot<T : Menu>(container: Container, id: Int, menu: T) : T7Slot<T>(container, id, menu) {
  override fun mayPlace(pStack: ItemStack): Boolean {
    return pStack.item == T7Items.RUNE.get()
  }

  companion object{
    val TEXTURE = Texture("gui/research_table/rune_slot", 18, 18)
  }
}
