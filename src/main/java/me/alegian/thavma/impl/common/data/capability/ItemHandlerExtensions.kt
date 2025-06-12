package me.alegian.thavma.impl.common.data.capability

import net.neoforged.neoforge.items.IItemHandler

val IItemHandler.firstStack
  get() = getStackInSlot(0)