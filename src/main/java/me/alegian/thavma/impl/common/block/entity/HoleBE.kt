package me.alegian.thavma.impl.common.block.entity

import com.mojang.serialization.codecs.RecordCodecBuilder
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HoleBE(pos: BlockPos, state: BlockState) : DataComponentBE(T7BlockEntities.HOLE.get(), pos, state) {
  private var lifetimeTicks = LIFETIME

  fun serverTick() {
    if (lifetimeTicks <= 0) {
      val state = get(T7DataComponents.HOLE_STATE)
      if (state != null) {
        level?.setBlock(blockPos, state.blockState, Block.UPDATE_CLIENTS)
      } else {
        level?.removeBlock(blockPos, false)
      }
    }
    lifetimeTicks--
  }

  companion object{
    val LIFETIME = 120

    data class HoleState(val blockState: BlockState, val direction: Direction){
      companion object{
        val CODEC = RecordCodecBuilder.create<HoleState>{
          it.group(
            BlockState.CODEC.fieldOf("blockState").forGetter { it.blockState },
            Direction.CODEC.fieldOf("direction").forGetter { it.direction },
          ).apply(it){b, d -> HoleState(b, d)}
        }
      }
    }
  }
}
