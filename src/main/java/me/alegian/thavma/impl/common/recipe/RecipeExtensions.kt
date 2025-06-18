package me.alegian.thavma.impl.common.recipe

import net.minecraft.Util
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeType

private val recipeTranslationCache = mutableMapOf<RecipeType<*>, String>()
val RecipeType<*>.translationId
  get() = recipeTranslationCache.getOrPut(this) {
    Util.makeDescriptionId("recipe", BuiltInRegistries.RECIPE_TYPE.getKey(this))
  }