package me.alegian.thavma.impl.client.gui.tooltip

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.aspect.getAspects
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack

class AspectTooltipComponent(itemStack: ItemStack) : TooltipComponent {
  var aspectMap: AspectMap? = null
    private set

  init {
    aspectMap = getAspects(itemStack.item)
  }
}
