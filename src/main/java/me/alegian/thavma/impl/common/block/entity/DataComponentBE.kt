package me.alegian.thavma.impl.common.block.entity

import me.alegian.thavma.impl.common.util.registry
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.MutableDataComponentHolder

private const val NBT_KEY = "componentMap"

abstract class DataComponentBE(pType: BlockEntityType<*>, pPos: BlockPos, pBlockState: BlockState) : BlockEntity(pType, pPos, pBlockState), MutableDataComponentHolder {
  var componentMap: PatchedDataComponentMap = PatchedDataComponentMap(DataComponentMap.EMPTY)

  override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
    return saveWithoutMetadata(lookupProvider)
  }

  override fun getUpdatePacket() =
    ClientboundBlockEntityDataPacket.create(this)

  override fun <T> set(componentType: DataComponentType<in T>, value: T?): T? {
    setChanged()
    return componentMap.set(componentType, value)
  }

  override fun <T> remove(componentType: DataComponentType<out T>): T? {
    setChanged()
    return componentMap.remove(componentType)
  }

  override fun applyComponents(patch: DataComponentPatch) {
    componentMap.applyPatch(patch)
  }

  override fun applyComponents(components: DataComponentMap) {
    componentMap.setAll(components)
  }

  override fun getComponents() = componentMap

  override fun collectImplicitComponents(components: DataComponentMap.Builder) {
    super.collectImplicitComponents(components)
    components.addAll(componentMap)
  }

  override fun applyImplicitComponents(componentInput: DataComponentInput) {
    super.applyImplicitComponents(componentInput)
    val componentTypes = level?.registry(Registries.DATA_COMPONENT_TYPE) ?: return
    for (type in componentTypes) applyInput(type, componentInput)
  }

  // this function serves only to help compiler with generics
  private fun <T> applyInput(type: DataComponentType<T>, input: DataComponentInput) {
    val value = input.get(type) ?: return
    componentMap.set(type, value)
  }

  override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
    super.loadAdditional(pTag, pRegistries)
    val patch = DataComponentPatch.CODEC.decode(NbtOps.INSTANCE, pTag.get(NBT_KEY)).getOrThrow().first
    componentMap = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch)
  }

  override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
    super.saveAdditional(pTag, pRegistries)
    val tag = DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, componentMap.asPatch()).orThrow
    pTag.put(NBT_KEY, tag)
  }
}
