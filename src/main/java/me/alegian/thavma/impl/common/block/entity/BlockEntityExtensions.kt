package me.alegian.thavma.impl.common.block.entity

import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.capabilities.Capabilities

val BlockEntity.itemHandler
  get() = level?.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, blockState, this, null)