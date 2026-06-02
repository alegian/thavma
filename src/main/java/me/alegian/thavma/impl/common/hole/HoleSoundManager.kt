package me.alegian.thavma.impl.common.hole

import me.alegian.thavma.impl.common.block.entity.HoleBE
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.neoforged.neoforge.event.tick.LevelTickEvent

// X ticks after opening hole, play closing sound
object HoleSoundManager{
  private val instances = mutableListOf<HoleSound>()

  fun levelTick(event: LevelTickEvent.Post) {
    val iterator = instances.iterator()
    while (iterator.hasNext()) {
      val instance = iterator.next()
      if (instance.level !== event.level) return
      if (instance.lifetimeTicks == 0) {
        iterator.remove()
        event.level.playSound(null, instance.blockPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS)
      } else instance.lifetimeTicks--
    }
  }

  fun create(blockPos: BlockPos, level: Level) =
    instances.add(HoleSound(HoleBE.LIFETIME, blockPos, level))


  private data class HoleSound(
    var lifetimeTicks: Int,
    val blockPos: BlockPos,
    val level: Level
  )
}