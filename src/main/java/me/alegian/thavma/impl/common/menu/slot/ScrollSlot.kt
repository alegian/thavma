package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.item.ResearchScrollItem
import me.alegian.thavma.impl.common.menu.Menu
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class ScrollSlot<T : Menu>(container: Container, id: Int, menu: T) : T7Slot<T>(container, id, menu) {
  override fun mayPlace(pStack: ItemStack): Boolean {
    return pStack.item is ResearchScrollItem
  }

  companion object{
    val TEXTURE = Texture("gui/research_table/scroll_slot", 18, 18)
  }
}
