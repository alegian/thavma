package me.alegian.thavma.impl.client.gui.tooltip

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.getAspects
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack

private var internalMap: AspectMap? = null

class AspectTooltipComponent(itemStack: ItemStack) : TooltipComponent {
  init {
    internalMap = getAspects(itemStack.item)
  }

  val aspectMap: AspectMap
    get() {
      return internalMap ?: AspectMap.EMPTY
    }
}
