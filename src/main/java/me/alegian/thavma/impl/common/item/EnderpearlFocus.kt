package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.init.registries.deferred.T7Attachments
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ThrownEnderpearl
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.neoforged.neoforge.event.entity.EntityTeleportEvent

class EnderpearlFocus : Item(
  Properties().stacksTo(1)
) {
  override fun doesSneakBypassUse(stack: ItemStack, level: LevelReader, pos: BlockPos, player: Player) = true

  override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val stackInHand = player.getItemInHand(usedHand)
    if (stackInHand.item !is WandItem) return InteractionResultHolder.pass(stackInHand)

    level.playSound(
      null,
      player.x,
      player.y,
      player.z,
      SoundEvents.ENDER_PEARL_THROW,
      SoundSource.NEUTRAL,
      0.5F,
      0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
    )
    player.cooldowns.addCooldown(stackInHand.item, 20)
    if (!level.isClientSide) {
      val thrownPearl = ThrownEnderpearl(level, player)
      thrownPearl.setData(T7Attachments.ENDERPEARL_NO_DAMAGE, true)
      thrownPearl.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F)
      level.addFreshEntity(thrownPearl)
    }

    return InteractionResultHolder.sidedSuccess(stackInHand, level.isClientSide())
  }

  companion object {
    fun enderpearlTeleport(event: EntityTeleportEvent.EnderPearl) {
      if (event.pearlEntity.getData(T7Attachments.ENDERPEARL_NO_DAMAGE))
        event.attackDamage = 0f
    }
  }
}