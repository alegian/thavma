package me.alegian.thavma.impl.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DataComponentBE extends BlockEntity implements MutableDataComponentHolder {
  final PatchedDataComponentMap components = new PatchedDataComponentMap(DataComponentMap.EMPTY);

  public DataComponentBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
  }

  @Override
  public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookupProvider) {
    return saveWithoutMetadata(lookupProvider);
  }

  @Nullable
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public <T> @Nullable T set(@NotNull DataComponentType<? super T> componentType, @Nullable T value) {
    this.setChanged();
    return this.components.set(componentType, value);
  }

  @Override
  public <T> @Nullable T remove(@NotNull DataComponentType<? extends T> componentType) {
    this.setChanged();
    return this.components.remove(componentType);
  }

  @Override
  public void applyComponents(@NotNull DataComponentPatch patch) {
    this.components.applyPatch(patch);
  }

  @Override
  public void applyComponents(@NotNull DataComponentMap components) {
    this.components.setAll(components);
  }

  @Override
  public @NotNull DataComponentMap getComponents() {
    return components;
  }

  public abstract DataComponentType<?>[] getComponentTypes();

  @Override
  protected void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
    super.loadAdditional(pTag, pRegistries);
    for (var componentType : getComponentTypes()) {
      String tagName = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType).toString();
      loadComponent(pTag.get(tagName), componentType);
    }
  }

  protected <T> void loadComponent(Tag pTag, DataComponentType<T> pComponentType) {
    if (pTag != null) {
      T loaded = pComponentType.codecOrThrow().decode(NbtOps.INSTANCE, pTag).getOrThrow().getFirst();
      set(pComponentType, loaded);
    }
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
    super.saveAdditional(pTag, pRegistries);
    var componentTypes = getComponentTypes();
    for (var componentType : componentTypes) {
      String tagName = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(componentType).toString();
      var tag = componentNBT(componentType);
      if (tag != null) pTag.put(tagName, tag);
    }
  }

  protected <T> Tag componentNBT(DataComponentType<T> pComponentType) {
    T value = get(pComponentType);
    if (value == null) return null;
    return pComponentType.codecOrThrow().encodeStart(NbtOps.INSTANCE, value).getOrThrow();
  }
}
