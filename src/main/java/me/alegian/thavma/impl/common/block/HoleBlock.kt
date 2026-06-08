package me.alegian.thavma.impl.common.block

import me.alegian.thavma.impl.common.block.entity.HoleBE
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.neoforged.neoforge.common.util.TriState

class HoleBlock : Block(
  Properties.of()
    .noCollission()
    .noOcclusion()
    .noTerrainParticles()
    .strength(-1.0F, 3600000.0F)
    .lightLevel { 7 }
    .isValidSpawn(Blocks::never)
    .pushReaction(PushReaction.BLOCK)
    .noLootTable()
), EntityBlock {
  override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HoleBE(pos, state)

  override fun getRenderShape(state: BlockState) = RenderShape.INVISIBLE

  override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = Shapes.empty()
  override fun getBlockSupportShape(state: BlockState, level: BlockGetter, pos: BlockPos) = Shapes.block()
  override fun getOcclusionShape(state: BlockState, level: BlockGetter, pos: BlockPos) = Shapes.block()
  override fun getVisualShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext) = Shapes.block()

  override fun canSustainPlant(state: BlockState, level: BlockGetter, soilPosition: BlockPos, facing: Direction, plant: BlockState) = TriState.TRUE

  override fun canBeReplaced(state: BlockState, fluid: Fluid) = false
  override fun canBeReplaced(state: BlockState, useContext: BlockPlaceContext) = false

  override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType) = false

  override fun getCloneItemStack(state: BlockState, target: HitResult, level: LevelReader, pos: BlockPos, player: Player) = ItemStack.EMPTY

  override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, type: BlockEntityType<T>) =
    BaseEntityBlock.createTickerHelper(type, T7BlockEntities.HOLE.get()) { _, _, _, be -> be.serverTick() }
}
