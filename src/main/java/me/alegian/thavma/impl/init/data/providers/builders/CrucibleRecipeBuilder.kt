package me.alegian.thavma.impl.init.data.providers.builders

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.recipe.CrucibleRecipe
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
import javax.naming.OperationNotSupportedException

class CrucibleRecipeBuilder(private val result: ItemStack, private val aspects: AspectMap, private val catalyst: Ingredient) : RecipeBuilder {
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

    val recipe = CrucibleRecipe(this.aspects, this.catalyst, this.result)

    val t7id = rl(id.path).withSuffix("_crucible")
    output.accept(t7id, recipe, advancement.build(t7id.withPrefix("recipes/")))
  }
}
