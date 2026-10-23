package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

val ItemLike.itemResourceKey
  get() = BuiltInRegistries.ITEM.getResourceKey(this.asItem()).orElseThrow()

var ItemStack.interactingBlockPos
  get() = get(T7DataComponents.INTERACTING_BLOCKPOS)
  set(value) {
    set(T7DataComponents.INTERACTING_BLOCKPOS, value)
  }

