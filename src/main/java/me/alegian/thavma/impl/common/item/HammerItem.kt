package me.alegian.thavma.impl.common.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.event.level.BlockEvent

/**
 * Mining hammer for 3x3 mining.
 * Has double durability of the corresponding pickaxe (via event),
 * but takes 9x more damage from mining (every block counts)
 */
class HammerItem(tier: Tier, props: Properties) : DiggerItem(tier, BlockTags.MINEABLE_WITH_PICKAXE, props) {
  fun getValid3x3PositionsExceptOrigin(hitResult: BlockHitResult, level: LevelAccessor, itemStack: ItemStack, entity: LivingEntity): MutableList<BlockPos> {
    val positions = mutableListOf<BlockPos>()
    val originBlockPos = hitResult.blockPos
    val originBlockState = level.getBlockState(originBlockPos)

    // doesn't do AoE if it cant break the original block, or if shifting
    if (!isCorrectToolForDrops(itemStack, originBlockState) || entity.isShiftKeyDown) return mutableListOf()

    // find the 2 axes perpendicular to the block hit direction
    val hitAxis = hitResult.direction.axis
    val allAxes = listOf(Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z)
    val perpendicularAxes = allAxes.stream().filter { a -> a !== hitAxis }.toList()

    // 3x3 area, except original block, only for correct mining tool
    for (i in -1..1)
      for (j in -1..1) {
        val currPos = originBlockPos
          .relative(perpendicularAxes[0], i)
          .relative(perpendicularAxes[1], j)
        val currBlockState = level.getBlockState(currPos)

        if ((i != 0 || j != 0) && isCorrectToolForDrops(itemStack, currBlockState)) positions.add(currPos)
      }

    return positions
  }

  companion object {
    private var allowHammerBreakEvents = true

    fun breakBlock(event: BlockEvent.BreakEvent) {
      // disallow nested hammer break events, to avoid infinite recursion
      if (!allowHammerBreakEvents) return

      val player = event.player
      if (player !is ServerPlayer) return

      val itemStack = player.mainHandItem
      val item = itemStack.item
      val level = event.level

      if (item is HammerItem) {
        allowHammerBreakEvents = false

        val hitResult = player.pick(player.blockInteractionRange(), 0f, false)
        if (hitResult is BlockHitResult && hitResult.type != HitResult.Type.MISS)
          for (pos in item.getValid3x3PositionsExceptOrigin(hitResult, level, itemStack, player))
            player.gameMode.destroyBlock(pos)

        allowHammerBreakEvents = true
      }
    }
  }
}
