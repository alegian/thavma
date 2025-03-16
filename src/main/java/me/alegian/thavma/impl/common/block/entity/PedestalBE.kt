package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.util.updateBlockEntityS2C
import me.alegian.thavma.impl.init.registries.deferred.T7BlockEntities
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar
import software.bernie.geckolib.util.GeckoLibUtil

/**
 * Default values used for rendering Item form
 */
class PedestalBE(
  pos: BlockPos = BlockPos(0, 0, 0),
  blockState: BlockState = T7Blocks.PEDESTAL.get().defaultBlockState()
) : BlockEntity(T7BlockEntities.PEDESTAL.get(), pos, blockState), GeoBlockEntity {
  private val cache = GeckoLibUtil.createInstanceCache(this)
  val inventory: ItemStackHandler = object : ItemStackHandler() {
    override fun getStackLimit(slot: Int, stack: ItemStack): Int {
      return 1
    }

    override fun onContentsChanged(slot: Int) {
      val level = level ?: return
      if (!level.isClientSide() && level is ServerLevel) level.updateBlockEntityS2C(blockPos)
    }
  }

  override fun registerControllers(controllers: ControllerRegistrar) {
  }

  override fun getAnimatableInstanceCache(): AnimatableInstanceCache {
    return this.cache
  }

  fun dropItem() {
    val container = SimpleContainer(inventory.getStackInSlot(0))

    level?.let { Containers.dropContents(it, blockPos, container) }
  }

  fun getItem(): ItemStack = inventory.getStackInSlot(0)

  override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
    super.saveAdditional(tag, registries)
    tag.put("inventory", inventory.serializeNBT(registries))
  }

  override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
    super.loadAdditional(tag, registries)
    inventory.deserializeNBT(registries, tag.getCompound("inventory"))
  }

  override fun getUpdatePacket(): Packet<ClientGamePacketListener?>? {
    return ClientboundBlockEntityDataPacket.create(this)
  }

  override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
    return saveWithoutMetadata(registries)
  }
}
