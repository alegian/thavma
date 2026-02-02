package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.level.Excavation
import me.alegian.thavma.impl.init.registries.deferred.Aspects
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level

class ExcavationFocus : Item(
  Properties().stacksTo(1)
) {
  fun aspectCost() = AspectMap.builder().add(Aspects.TERRA, 1).build()

  override fun getUseDuration(stack: ItemStack, entity: LivingEntity) = 72000

  override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val stack = player.getItemInHand(usedHand)
    if (stack.item !is WandItem || !hasEnoughAspects(stack)) return InteractionResultHolder.pass(stack)

    player.startUsingItem(usedHand)
    return InteractionResultHolder.consume(stack)
  }

  override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
    if (
      level.gameTime % 4 != 0L ||
      level.isClientSide ||
      livingEntity !is Player
    ) return

    if (!hasEnoughAspects(stack)) return livingEntity.releaseUsingItem()

    if (level.gameTime % 20 == 0L) AspectContainer.from(stack)?.extract(aspectCost())
    advanceBlockBreak(level, livingEntity)
  }

  override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int) {
    if (
      level.isClientSide ||
      livingEntity !is Player
    ) return
    Excavation.stopExcavation(level, livingEntity)
  }

  private fun advanceBlockBreak(level: Level, player: Player) {
    val from = player.eyePosition
    val to = from.add(player.getViewVector(0f).scale(Excavation.RANGE))
    val hitresult = level.clip(ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player))
    Excavation.excavate(level, player, hitresult, 4)
  }

  private fun hasEnoughAspects(stack: ItemStack) =
    AspectContainer.from(stack)?.aspects?.contains(aspectCost()) ?: false
}