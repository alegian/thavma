package me.alegian.thavma.impl.common.util

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

fun ServerLevel.updateBlockEntityS2C(blockPos: BlockPos) {
  getBlockEntity(blockPos)?.let { be ->
    be.setChanged()
    sendBlockUpdated(blockPos, be.blockState, be.blockState, Block.UPDATE_CLIENTS)
  }
}

fun <T : BlockEntity> Level.getBE(blockPos: BlockPos?, type: BlockEntityType<T>): T? {
  if (blockPos == null) return null
  return getBlockEntity(blockPos, type).orElse(null)
}

fun <K : RecipeInput, T : Recipe<K>> Level.getRecipe(type: Supplier<RecipeType<T>>, input: K) = recipeManager.getRecipeFor(type.get(), input, this).getOrNull()?.value()
