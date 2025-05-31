package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.block.PillarBlock.Companion.multiblockPositions
import me.alegian.thavma.impl.common.util.getBE
import me.alegian.thavma.impl.init.registries.T7BlockStateProperties.MASTER
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities.PILLAR
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.world.phys.AABB
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.util.GeckoLibUtil
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import kotlin.jvm.optionals.getOrNull

private val MASTER_POS_NBT_KEY = "masterPos"

/**
 * Default values used for rendering Item form
 */
class PillarBE(
  pos: BlockPos = BlockPos(0, 0, 0),
  blockState: BlockState = T7Blocks.PILLAR.get().defaultBlockState()
) : BlockEntity(PILLAR.get(), pos, blockState), GeoBlockEntity {
  val renderAABB = AABB(blockPos).expandTowards(blockState.getValue(HORIZONTAL_FACING).normal.toVec3()).expandTowards(0.0, 1.0, 0.0)
  private val cache = GeckoLibUtil.createInstanceCache(this)
  var masterPos: BlockPos? = null

  override fun registerControllers(controllers: ControllerRegistrar) {
  }

  override fun getAnimatableInstanceCache() = cache

  /**
   * slaves delegate to the master
   * masters break everything (including themselves)
   */
  fun breakMultiblock() {
    if (!blockState.getValue(MASTER)) {
      level?.getBE(masterPos, PILLAR.get())?.breakMultiblock()
    } else {
      for (pos in multiblockPositions(blockPos, blockState.getValue(HORIZONTAL_FACING))) {
        level?.destroyBlock(pos, true)
      }
    }
  }

  override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
    super.loadAdditional(tag, registries)
    masterPos = NbtUtils.readBlockPos(tag, MASTER_POS_NBT_KEY).getOrNull()
  }

  override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
    super.saveAdditional(tag, registries)
    masterPos?.let {
      val masterPosTag = NbtUtils.writeBlockPos(it)
      tag.put(MASTER_POS_NBT_KEY, masterPosTag)
    }
  }
}
