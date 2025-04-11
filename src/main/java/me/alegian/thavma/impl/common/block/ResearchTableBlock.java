package me.alegian.thavma.impl.common.block;

import me.alegian.thavma.impl.common.block.entity.ResearchTableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

/**
 * 2-block system like bed. Loot table drops nothing for FOOT and drops self for HEAD
 */
public class ResearchTableBlock extends Block implements EntityBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;

  public ResearchTableBlock() {
    super(Properties.ofFullCopy(Blocks.OAK_PLANKS));
    this.registerDefaultState(stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, PART);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Direction direction = context.getHorizontalDirection();
    BlockPos secondPos = context.getClickedPos().relative(direction);
    Level level = context.getLevel();
    return level.getBlockState(secondPos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(secondPos)
        ? this.defaultBlockState().setValue(FACING, direction)
        : null;
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);
    if (!level.isClientSide) {
      BlockPos secondPos = pos.relative(state.getValue(FACING));
      level.setBlock(secondPos, state.setValue(PART, BedPart.HEAD), Block.UPDATE_ALL);
      level.blockUpdated(pos, Blocks.AIR);
      state.updateNeighbourShapes(level, pos, Block.UPDATE_ALL);
    }
  }

  /**
   * for creative, break the 2nd block before it has a chance to updateShape, to prevent item drops
   */
  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    if (!level.isClientSide && player.isCreative()) {
      BedPart bedpart = state.getValue(PART);
      if (bedpart == BedPart.FOOT) {
        BlockPos secondPos = pos.relative(getNeighbourDirection(bedpart, state.getValue(FACING)));
        BlockState secondState = level.getBlockState(secondPos);
        if (secondState.is(this)) {
          level.setBlock(secondPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);
          level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, secondPos, Block.getId(secondState));
        }
      }
    }

    return super.playerWillDestroy(level, pos, state, player);
  }

  /**
   * breaks foot after breaking head, and vice versa.
   * responsible for item drops when breaking the FOOT part
   */
  @Override
  protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
    if (facing == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
      return facingState.is(this) && facingState.getValue(PART) != state.getValue(PART)
          ? state
          : Blocks.AIR.defaultBlockState();
    } else {
      return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
  }

  private static Direction getNeighbourDirection(BedPart part, Direction direction) {
    return part == BedPart.FOOT ? direction : direction.getOpposite();
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ResearchTableBE(pos, state);
  }

//  @Override
//  protected RenderShape getRenderShape(BlockState state) {
//    return RenderShape.ENTITYBLOCK_ANIMATED;
//  }
}
