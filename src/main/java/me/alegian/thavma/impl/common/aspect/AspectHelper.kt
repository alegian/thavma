package me.alegian.thavma.impl.common.aspect

import me.alegian.thavma.impl.init.registries.T7DataMaps.AspectContent.BLOCK
import me.alegian.thavma.impl.init.registries.T7DataMaps.AspectContent.ENTITY
import me.alegian.thavma.impl.init.registries.T7DataMaps.AspectContent.ITEM
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

fun getAspects(itemEntity: ItemEntity): AspectMap? {
  return getAspects(itemEntity.item)
}

/**
 * This method checks for Block aspects before returning Item aspects.
 */
fun getAspects(itemLike: ItemLike): AspectMap? {
  return when (val item = itemLike.asItem()) {
    is BlockItem -> BuiltInRegistries.BLOCK.wrapAsHolder(item.block).getData(BLOCK)
    else -> BuiltInRegistries.ITEM.wrapAsHolder(item).getData(ITEM)
  }
}

fun getAspects(entity: Entity): AspectMap? {
  if (entity is ItemEntity) return getAspects(entity.item)
  return BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.type).getData(ENTITY)
}

fun getAspects(itemStack: ItemStack): AspectMap? {
  return getAspects(itemStack.item)?.scale(itemStack.count)
}
