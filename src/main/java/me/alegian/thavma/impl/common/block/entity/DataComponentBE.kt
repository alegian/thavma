package me.alegian.thavma.impl.common.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.MutableDataComponentHolder

abstract class DataComponentBE(pType: BlockEntityType<*>, pPos: BlockPos, pBlockState: BlockState) : BlockEntity(pType, pPos, pBlockState), MutableDataComponentHolder {
  val componentMap: PatchedDataComponentMap = PatchedDataComponentMap(DataComponentMap.EMPTY)
  abstract val componentTypes: Array<DataComponentType<*>>

  override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
    return saveWithoutMetadata(lookupProvider)
  }

  override fun getUpdatePacket(): ClientboundBlockEntityDataPacket? {
    return ClientboundBlockEntityDataPacket.create(this)
  }

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

  override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
    super.loadAdditional(pTag, pRegistries)
    for (componentType in this.componentTypes) {
      val tagName = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType).toString()
      loadComponent(pTag.get(tagName), componentType)
    }
  }

  protected fun <T> loadComponent(pTag: Tag?, pComponentType: DataComponentType<T>) {
    if (pTag != null) {
      val loaded = pComponentType.codecOrThrow().decode(NbtOps.INSTANCE, pTag).getOrThrow().getFirst()
      set(pComponentType, loaded)
    }
  }

  override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
    super.saveAdditional(pTag, pRegistries)
    for (componentType in componentTypes) {
      val tagName = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType).toString()
      val tag = componentNBT(componentType)
      if (tag != null) pTag.put(tagName, tag)
    }
  }

  protected fun <T> componentNBT(pComponentType: DataComponentType<T>): Tag? {
    val value = get(pComponentType)
    if (value == null) return null
    return pComponentType.codecOrThrow().encodeStart(NbtOps.INSTANCE, value).getOrThrow()
  }
}
