package me.alegian.thavma.impl.common.item;

import me.alegian.thavma.impl.common.entity.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Mining hammer for 3x3 mining.
 * Has double durability of the corresponding pickaxe (via event),
 * but takes 9x more damage from mining (every block counts)
 */
public class HammerItem extends DiggerItem {
  public HammerItem(Tier tier, Item.Properties props) {
    super(tier, BlockTags.MINEABLE_WITH_PICKAXE, props);
  }

  /**
   * Used in event
   */
  public void tryBreak3x3exceptOrigin(ServerPlayer serverPlayer, LevelAccessor level, ItemStack itemStack) {
    var hitResult = EntityHelper.INSTANCE.getServerHitResult(serverPlayer);
    for (var pos : this.getValid3x3PositionsExceptOrigin(hitResult, level, itemStack, serverPlayer))
      serverPlayer.gameMode.destroyBlock(pos);
  }

  public List<BlockPos> getValid3x3PositionsExceptOrigin(BlockHitResult hitResult, LevelAccessor level, ItemStack itemStack, LivingEntity entity) {
    var positions = new ArrayList<BlockPos>();
    var originBlockPos = hitResult.getBlockPos();
    var originBlockState = level.getBlockState(originBlockPos);

    // doesn't do AoE if it cant break the original block, or if crouching
    if (!this.isCorrectToolForDrops(itemStack, originBlockState) || entity.isCrouching()) return List.of();

    // find the 2 axes perpendicular to the block hit direction
    var hitAxis = hitResult.getDirection().getAxis();
    var allAxes = List.of(Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z);
    var perpendicularAxes = allAxes.stream().filter(a -> a != hitAxis).toList();

    // 3x3 area, except original block, only for correct mining tool
    for (int i = -1; i <= 1; i++)
      for (int j = -1; j <= 1; j++) {
        var currPos = originBlockPos
            .relative(perpendicularAxes.get(0), i)
            .relative(perpendicularAxes.get(1), j);
        var currBlockState = level.getBlockState(currPos);

        if ((i != 0 || j != 0) && this.isCorrectToolForDrops(itemStack, currBlockState))
          positions.add(currPos);
      }

    return positions;
  }
}
