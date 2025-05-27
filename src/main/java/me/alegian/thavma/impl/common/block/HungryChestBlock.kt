package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.block.entity.HungryChestBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ChestBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.ChestType

class HungryChestBlock : ChestBlock(
  Properties.ofFullCopy(Blocks.CHEST),
  { -> T7BlockEntities.HUNGRY_CHEST.get() }
) {
  override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
    return super.getStateForPlacement(context)?.setValue(BlockStateProperties.CHEST_TYPE, ChestType.SINGLE)
  }

  override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? {
    return if (type === T7BlockEntities.HUNGRY_CHEST.get()) BlockEntityTicker(HungryChestBE::tick) else null
  }

  override fun newBlockEntity(pos: BlockPos, state: BlockState) = HungryChestBE(pos, state)

  companion object{
    val CONTAINER_TITLE = "container." + Thavma.MODID + ".hungry_chest"
  }
}