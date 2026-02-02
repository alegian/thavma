package me.alegian.thavma.impl.common.item

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.ItemLike

val ItemLike.itemResourceKey
  get() = BuiltInRegistries.ITEM.getResourceKey(this.asItem()).orElseThrow()

