package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.ItemHandlerHelper.insertItemStacked

private val SUCK_AREA = Block.box(.0, .0, .0, 16.0, 32.0, 16.0).toAabbs().first()

// default values used in item renderer
class HungryChestBE(
  pos: BlockPos = BlockPos.ZERO,
  state: BlockState = T7Blocks.HUNGRY_CHEST.get().defaultBlockState()
) : ChestBlockEntity(T7BlockEntities.HUNGRY_CHEST.get(), pos, state) {
  private val capCache by lazy {
    level?.let {
      if (it !is ServerLevel) null
      else BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, it, pos, null)
    }
  }

  companion object {
    fun tick(level: Level, pos: BlockPos, state: BlockState, be: BlockEntity) {
      if (level.isClientSide) return
      if (be !is HungryChestBE) return

      val iih = be.capCache?.capability ?: return
      val itemEntities = level.getEntitiesOfClass(ItemEntity::class.java, SUCK_AREA.move(pos)) { true }

      for (entity in itemEntities) {
        val remainder = insertItemStacked(iih, entity.item, false)
        if (!remainder.isEmpty) entity.item = remainder
        else entity.kill()
      }
    }
  }
}