package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.menu.WorkbenchMenu
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes.WORKBENCH
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

class WorkbenchResultSlot(menu: WorkbenchMenu, id: Int) : T7ResultSlot<WorkbenchMenu>(menu, menu.craftingContainer, menu.resultContainer, id) {
  /**
   * On taking result, also remove vis from wand.
   * TODO: support craft remainders
   */
  override fun onTake(player: Player, stack: ItemStack) {
    val craftingContainer = menu.craftingContainer
    val positionedInput = craftingContainer.asPositionedCraftInput()
    val craftingInput = positionedInput.input()
    val i = positionedInput.left()
    val j = positionedInput.top()
    val recipe = player.level().recipeManager.getRecipeFor(WORKBENCH.get(), craftingInput, player.level()).getOrNull()

    for (k in 0..<craftingInput.height()) for (l in 0..<craftingInput.width()) {
      val currSlotIndex = l + i + (k + j) * craftingContainer.width
      val currItem = craftingContainer.getItem(currSlotIndex)
      if (!currItem.isEmpty) craftingContainer.removeItem(currSlotIndex, 1)
    }

    val container = AspectContainer.from(menu.wandContainer.getItem(0))
    if (container == null || recipe == null) return
    container.extract(recipe.value().aspects)
  }
}
