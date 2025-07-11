package me.alegian.thavma.impl.common.aspect

import me.alegian.thavma.impl.init.registries.T7DataMaps.AspectContent.ENTITY
import me.alegian.thavma.impl.init.registries.T7DataMaps.AspectContent.ITEM
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

/**
 * Blocks fall back to their items
 */
fun getAspects(itemLike: ItemLike) =
  BuiltInRegistries.ITEM.wrapAsHolder(itemLike.asItem()).getData(ITEM)

fun getAspects(entity: Entity): AspectMap? {
  if (entity is ItemEntity) return getAspects(entity.item)
  return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.type).getData(ENTITY)
}

fun getAspects(itemStack: ItemStack): AspectMap? {
  return getAspects(itemStack.item)?.scale(itemStack.count)
}
