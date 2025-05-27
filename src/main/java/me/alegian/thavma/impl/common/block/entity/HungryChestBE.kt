package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.block.HungryChestBlock
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack.matches
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
  private var openTicksLeft = 0
  private var open = false

  override fun getDefaultName() = Component.translatable(HungryChestBlock.CONTAINER_TITLE)

  companion object {
    fun tick(level: Level, pos: BlockPos, state: BlockState, be: BlockEntity) {
      if (be !is HungryChestBE) return

      if (level.isClientSide) return lidAnimateTick(level, pos, state, be)

      val iih = be.capCache?.capability ?: return
      val itemEntities = level.getEntitiesOfClass(ItemEntity::class.java, SUCK_AREA.move(pos)) { true }

      for (entity in itemEntities) {
        val remainder = insertItemStacked(iih, entity.item, false)
        if (!matches(remainder, entity.item)) {
          if (!be.open){
            be.signalOpenCount(level, pos, state, 0, 1)
            be.open = true
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1f, 1.0f)
          }
          be.openTicksLeft = 3
        }
        if (!remainder.isEmpty) entity.item = remainder
        else entity.kill()
      }

      if (itemEntities.isEmpty() && be.open)
        if (be.openTicksLeft > 0) be.openTicksLeft--
        else{
          be.signalOpenCount(level, pos, state, 1, 0)
          be.open = false
        }
    }
  }
}