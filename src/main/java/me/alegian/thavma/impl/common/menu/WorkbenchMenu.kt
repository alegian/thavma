package me.alegian.thavma.impl.common.menu

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.common.menu.container.CraftingContainer3x3
import me.alegian.thavma.impl.common.menu.container.WandContainer
import me.alegian.thavma.impl.common.menu.container.WorkbenchResultContainer
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.ARCANE_WORKBENCH
import me.alegian.thavma.impl.init.registries.deferred.T7MenuTypes
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.item.ItemStack

class WorkbenchMenu(pContainerId: Int, pPlayerInventory: Inventory, private val levelAccess: ContainerLevelAccess = ContainerLevelAccess.NULL) : Menu(T7MenuTypes.WORKBENCH.get(), pContainerId, pPlayerInventory) {
  val craftingContainer = CraftingContainer3x3(this)
  val wandContainer = WandContainer<WorkbenchMenu>(this)
  val resultContainer = WorkbenchResultContainer(this)
  var requiredAspects = AspectMap()
    private set
  override val quickMovePriorities = listOf(wandContainer, craftingContainer)

  /**
   * Slot index must be container unique, but not necessarily menu unique
   */
  init {
    craftingContainer.addSlots()
    wandContainer.addSlots()
    inventory.addSlots()
    resultContainer.addSlots()

    addSlotListener(this)
  }

  private fun refreshRecipeResult() {
    val optionalRecipeHolder = level.recipeManager.getRecipeFor(T7RecipeTypes.WORKBENCH.get(), craftingContainer.asCraftInput(), level)

    requiredAspects = optionalRecipeHolder.map { r -> r.value().aspects }.orElse(AspectMap())

    if (!level.isClientSide()) {
      val resultItem = optionalRecipeHolder.map { r -> r.value().result }.orElse(ItemStack.EMPTY)

      resultContainer.setItem(0, resultItem)
    }

    resultContainer.setSlotEnabled(0, wandContainer.contains(requiredAspects))
  }

  override fun removed(pPlayer: Player) {
    super.removed(pPlayer)
    levelAccess.execute { level, blockPos ->
      clearContainer(pPlayer, craftingContainer)
      clearContainer(pPlayer, wandContainer)
    }
  }

  override fun stillValid(pPlayer: Player) =
    stillValid(levelAccess, pPlayer, ARCANE_WORKBENCH.get())

  override fun slotChanged(pContainerToSend: AbstractContainerMenu, pDataSlotIndex: Int, pStack: ItemStack) {
    refreshRecipeResult()
  }
}
