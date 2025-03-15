package me.alegian.thavma.impl.common.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

abstract class T7ItemRecipe<T : RecipeInput>(result: ItemStack) : Recipe<T> {
  val result = result
    get() = field.copy()

  override fun assemble(input: T, lookupProvider: HolderLookup.Provider): ItemStack {
    throw UnsupportedOperationException()
  }

  override fun canCraftInDimensions(width: Int, height: Int): Boolean {
    throw UnsupportedOperationException()
  }

  /**
   * For recipe book & JEI
   */
  override fun getResultItem(lookupProvider: HolderLookup.Provider): ItemStack {
    return this.result
  }
}