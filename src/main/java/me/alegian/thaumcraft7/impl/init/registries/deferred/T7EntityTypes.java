package me.alegian.thaumcraft7.impl.init.registries.deferred;

import me.alegian.thaumcraft7.impl.Thaumcraft;
import me.alegian.thaumcraft7.impl.common.entity.RendererEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class T7EntityTypes {
  public static final DeferredRegister<EntityType<?>> REGISTRAR = DeferredRegister.create(Registries.ENTITY_TYPE, Thaumcraft.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<ItemEntity>> FANCY_ITEM = REGISTRAR.register("fancy_item", () -> EntityType.Builder.<ItemEntity>of(ItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).eyeHeight(0.2125F).clientTrackingRange(6).updateInterval(20).build("fancy_item"));
  public static final DeferredHolder<EntityType<?>, EntityType<RendererEntity>> RENDERER = REGISTRAR.register("renderer", () -> EntityType.Builder.<RendererEntity>of((entityType, level) -> new RendererEntity(level, new Vec3(0, 0, 0)), MobCategory.MISC).sized(0.25F, 0.25F).eyeHeight(0.2125F).clientTrackingRange(6).updateInterval(20).build("renderer"));

}
