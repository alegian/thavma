package me.alegian.thavma.impl.common.menu

import me.alegian.thavma.impl.common.menu.container.RuneContainer
import me.alegian.thavma.impl.common.menu.container.ScrollContainer
import me.alegian.thavma.impl.common.research.SocketState
import me.alegian.thavma.impl.common.util.Indices
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks.RESEARCH_TABLE
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import me.alegian.thavma.impl.init.registries.deferred.T7MenuTypes.RESEARCH
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.item.ItemStack

class ResearchMenu(pContainerId: Int, pPlayerInventory: Inventory, private val levelAccess: ContainerLevelAccess = ContainerLevelAccess.NULL) : Menu(RESEARCH.get(), pContainerId, pPlayerInventory) {
  val scrollContainer = ScrollContainer(this)
  val runeContainer = RuneContainer(this)
  override val quickMovePriorities = listOf(scrollContainer, runeContainer)
  var researchState: Map<Indices, SocketState>? = null
  var completed = false

  /**
   * Slot index must be container unique, but not necessarily menu unique
   */
  init {
    runeContainer.addSlots()
    scrollContainer.addSlots()
    inventory.addSlots()

    addSlotListener(this)
  }

  override fun removed(pPlayer: Player) {
    super.removed(pPlayer)
    levelAccess.execute { level, blockPos ->
      clearContainer(pPlayer, runeContainer)
      clearContainer(pPlayer, scrollContainer)
    }
  }

  override fun stillValid(pPlayer: Player) = stillValid(levelAccess, pPlayer, RESEARCH_TABLE.get())

  override fun slotChanged(containerToSend: AbstractContainerMenu, dataSlotIndex: Int, stack: ItemStack) {
  }

  override fun slotsChanged(container: Container) {
    if (container !is ScrollContainer<*>) return
    val scrollState = scrollContainer.getItem(0).get(T7DataComponents.RESEARCH_STATE)
    researchState = scrollState?.socketStates?.associateBy { it.indices }
    val newCompleted = scrollState?.completed ?: false
    if (newCompleted && !completed && level.isClientSide)
      level.playSound(player, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.4f, 1f)
    completed = newCompleted
  }
}
