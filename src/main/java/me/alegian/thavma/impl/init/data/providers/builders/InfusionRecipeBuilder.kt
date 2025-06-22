package me.alegian.thavma.impl.init.data.providers.builders

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.recipe.InfusionRecipe
import me.alegian.thavma.impl.rl
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import javax.naming.OperationNotSupportedException

class InfusionRecipeBuilder(private val result: ItemStack, private val base: Ingredient, private val ingredients: List<Ingredient>, private val aspects: AspectMap) : RecipeBuilder {
  constructor(result: ItemLike, base: Ingredient, ingredients: List<Ingredient>, aspects: AspectMap) :
      this(result.asItem().defaultInstance, base, ingredients, aspects)

  override fun unlockedBy(name: String, criterion: Criterion<*>) =
    throw OperationNotSupportedException()

  override fun group(group: String?) =
    throw OperationNotSupportedException()

  override fun getResult(): Item {
    return result.item
  }

  override fun save(output: RecipeOutput, id: ResourceLocation) {
    val advancement = output.advancement()
      .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
      .rewards(AdvancementRewards.Builder.recipe(id))
      .requirements(AdvancementRequirements.Strategy.OR)

    val recipe = InfusionRecipe(base, result, ingredients, aspects)

    val t7id = rl(id.path).withSuffix("_infusion")
    output.accept(t7id, recipe, advancement.build(t7id.withPrefix("recipes/")))
  }
}
