package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.client.particle.CrucibleBubbleParticle
import me.alegian.thavma.impl.common.data.capability.CrucibleFluidHandler
import me.alegian.thavma.impl.init.registries.T7BlockStateProperties
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.CRUCIBLE
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents.ASPECTS
import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes.CRUCIBLE_BUBBLE
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.*

open class CrucibleBE(pos: BlockPos, blockState: BlockState) :
  DataComponentBE(CRUCIBLE.get(), pos, blockState) {
  val fluidHandler by lazy { CrucibleFluidHandler(this) }
  private val particles = ArrayList<CrucibleBubbleParticle>()
  override val componentTypes: Array<DataComponentType<*>>
    get() = arrayOf(ASPECTS.get())

  val waterHeight: Double
    get() {
      val amount = fluidHandler.fluidAmount
      val total = fluidHandler.capacity
      val percent = (amount / total.toDouble())

      return 3 / 16f + 12 / 16f * percent
    }

  fun addParticle(particle: CrucibleBubbleParticle) {
    particles.add(particle)
  }

  fun removeParticle(particle: CrucibleBubbleParticle) {
    particles.remove(particle)
  }

  protected open fun clearParticles() {
    particles.forEach(CrucibleBubbleParticle::scheduleRemove)
  }

  override fun onDataPacket(
    net: Connection,
    pkt: ClientboundBlockEntityDataPacket,
    lookupProvider: HolderLookup.Provider
  ) {
    // empty fluidstacks make empty tags, which are normally not handled
    // however we need to still update when tank empties
    this.loadAdditional(Objects.requireNonNull(pkt.tag), lookupProvider)
    // delete floating particles from previous water level
    this.clearParticles()
  }

  override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
    super.loadAdditional(pTag, pRegistries)
    fluidHandler.readFromNBT(pRegistries, pTag)
  }

  override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
    super.saveAdditional(pTag, pRegistries)
    fluidHandler.writeToNBT(pRegistries, pTag)
  }

  companion object {
    fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?) {
      if (level is ServerLevel
        && state.getValue(T7BlockStateProperties.BOILING)
        && level.gameTime % 7 == 0L && blockEntity is CrucibleBE
        && !blockEntity.fluidHandler.isEmpty
      ) level.sendParticles(
        CRUCIBLE_BUBBLE.get(),
        pos.x.toDouble() + level.random.nextDouble() * 12 / 16f + 2 / 16f,
        pos.y + blockEntity.waterHeight,
        pos.z.toDouble() + level.random.nextDouble() * 12 / 16f + 2 / 16f,
        1,
        0.0,
        0.0,
        0.0,
        0.0
      )
    }
  }
}
