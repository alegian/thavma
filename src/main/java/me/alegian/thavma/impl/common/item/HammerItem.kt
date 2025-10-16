package me.alegian.thavma.impl.common.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

/**
 * Mining hammer for 3x3 mining.
 * Has double durability of the corresponding pickaxe (via event),
 * but takes 9x more damage from mining (every block counts)
 */
class HammerItem(tier: Tier, props: Properties) : DiggerItem(tier, BlockTags.MINEABLE_WITH_PICKAXE, props) {
  fun getValid3x3PositionsExceptOrigin(pos: BlockPos, direction: Direction, level: LevelAccessor, itemStack: ItemStack, entity: LivingEntity): MutableList<BlockPos> {
    val positions = mutableListOf<BlockPos>()

    // doesn't do AoE if shifting
    if (entity.isShiftKeyDown) return mutableListOf()

    // find the 2 axes perpendicular to the block hit direction
    val hitAxis = direction.axis
    val allAxes = listOf(Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z)
    val perpendicularAxes = allAxes.stream().filter { a -> a !== hitAxis }.toList()

    // 3x3 area, except original block, only for correct mining tool
    for (i in -1..1)
      for (j in -1..1) {
        val currPos = pos
          .relative(perpendicularAxes[0], i)
          .relative(perpendicularAxes[1], j)
        val currBlockState = level.getBlockState(currPos)

        if ((i != 0 || j != 0) && isCorrectToolForDrops(itemStack, currBlockState)) positions.add(currPos)
      }

    return positions
  }

  companion object {
    private var allowHammerBreakEvents = true

    fun destroyBlockMixin(player: Player, level: Level, pos: BlockPos) {
      // disallow nested hammer breaks, to avoid infinite recursion
      if (!allowHammerBreakEvents) return

      if (player !is ServerPlayer) return

      val itemStack = player.mainHandItem
      val item = itemStack.item

      if (item is HammerItem) {
        allowHammerBreakEvents = false

        val hitResult = player.pick(player.blockInteractionRange(), 0f, false)
        if (hitResult is BlockHitResult && hitResult.type != HitResult.Type.MISS)
          for (pos in item.getValid3x3PositionsExceptOrigin(pos, hitResult.direction, level, itemStack, player))
            player.gameMode.destroyBlock(pos)

        allowHammerBreakEvents = true
      }
    }
  }
}
