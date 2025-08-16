package me.alegian.thavma.impl.common.item

import net.minecraft.core.BlockPos
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.EnderChestBlock

class EnderChestFocus : Item(
  Properties().stacksTo(1)
) {
  override fun doesSneakBypassUse(stack: ItemStack, level: LevelReader, pos: BlockPos, player: Player) = true

  override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val stackInHand = player.getItemInHand(usedHand)
    if (stackInHand.item !is WandItem) return InteractionResultHolder.pass(stackInHand)

    val container = player.enderChestInventory
    player.openMenu(SimpleMenuProvider({ id, inv, player ->
      ChestMenu.threeRows(id, inv, container)
    }, EnderChestBlock.CONTAINER_TITLE))
    player.awardStat(Stats.OPEN_ENDERCHEST)
    return InteractionResultHolder.consume(stackInHand)
  }
}