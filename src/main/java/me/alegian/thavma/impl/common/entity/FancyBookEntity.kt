package me.alegian.thavma.impl.common.entity

import me.alegian.thavma.impl.init.registries.deferred.T7EntityTypes.FANCY_ITEM
import me.alegian.thavma.impl.init.registries.deferred.T7Items.BOOK
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * Shorthand to create book ItemEntities after right-clicking bookcases with a wand.
 * Does not bob up and down, always spawns at the center of the bookcase, has default pickup delay.
 */
class FancyBookEntity(pLevel: Level, blockPos: BlockPos) : ItemEntity(FANCY_ITEM.get(), pLevel) {
  init {
    setPos(blockPos.x + .5, blockPos.y + .5, blockPos.z + .5)
    setDeltaMovement(0.0, 0.0, 0.0)
    val itemStack = ItemStack(BOOK.get())
    item = itemStack
    setDefaultPickUpDelay()
    lifespan = itemStack.getEntityLifespan(pLevel)
  }
}
