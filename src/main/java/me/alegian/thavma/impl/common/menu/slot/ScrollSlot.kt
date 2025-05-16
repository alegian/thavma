package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.common.item.ResearchScrollItem
import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class ScrollSlot<T : Menu>(container: Container, id: Int, menu: T) : T7Slot<T>(container, id, menu) {
  override fun mayPlace(pStack: ItemStack): Boolean {
    if (pStack.item !is ResearchScrollItem) return false
    val completed = pStack.get(T7DataComponents.RESEARCH_STATE)?.completed ?: false
    return !completed
  }

  companion object {
    val TEXTURE = Texture("gui/research_table/scroll_slot", 18, 18)
  }
}
