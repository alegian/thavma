package me.alegian.thavma.impl.common.menu

import me.alegian.thavma.impl.common.menu.container.RuneContainer
import me.alegian.thavma.impl.common.menu.container.ScrollContainer
import me.alegian.thavma.impl.common.menu.container.T7Container
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7MenuTypes.RESEARCH
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.item.ItemStack

class ResearchMenu(pContainerId: Int, pPlayerInventory: Inventory, private val levelAccess: ContainerLevelAccess = ContainerLevelAccess.NULL) : Menu(RESEARCH.get(), pContainerId, pPlayerInventory) {
  val scrollContainer = ScrollContainer(this)
  val runeContainer = RuneContainer(this)

  /**
   * Slot index must be container unique, but not necessarily menu unique
   */
  init {
    runeContainer.addSlots()
    scrollContainer.addSlots()
    playerInventory.addSlots()

    addSlotListener(this)
  }

  override fun getQuickMovePriorities(): MutableList<T7Container> {
    return mutableListOf<T7Container>(scrollContainer, runeContainer)
  }

  override fun removed(pPlayer: Player) {
    super.removed(pPlayer)
    levelAccess.execute { level, blockPos ->
      clearContainer(pPlayer, runeContainer)
      clearContainer(pPlayer, scrollContainer)
    }
  }

  override fun stillValid(pPlayer: Player): Boolean {
    return stillValid(levelAccess, pPlayer, RESEARCH_TABLE.get())
  }

  override fun slotChanged(containerToSend: AbstractContainerMenu, dataSlotIndex: Int, stack: ItemStack) {
  }
}
