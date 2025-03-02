package me.alegian.thavma.impl.common.data.capability

import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.type.capability.ICurio

class SimpleCurio(private val stack: ItemStack) : ICurio {
  override fun getStack(): ItemStack {
    return stack
  }
}
