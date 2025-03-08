package me.alegian.thavma.impl.common.menu;

import me.alegian.thavma.impl.common.aspect.AspectMap;
import me.alegian.thavma.impl.common.menu.container.CraftingContainer3x3;
import me.alegian.thavma.impl.common.menu.container.T7Container;
import me.alegian.thavma.impl.common.menu.container.WandContainer;
import me.alegian.thavma.impl.common.menu.container.WorkbenchResultContainer;
import me.alegian.thavma.impl.init.registries.deferred.T7Blocks;
import me.alegian.thavma.impl.init.registries.deferred.T7MenuTypes;
import me.alegian.thavma.impl.init.registries.deferred.T7RecipeTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkbenchMenu extends Menu {
  private final ContainerLevelAccess levelAccess;
  private final CraftingContainer3x3 craftingContainer = new CraftingContainer3x3(this);
  private final WandContainer<WorkbenchMenu> wandContainer = new WandContainer<>(this);
  private final WorkbenchResultContainer resultContainer = new WorkbenchResultContainer(this);
  private AspectMap requiredAspects = new AspectMap();

  public WorkbenchMenu(int pContainerId, Inventory pPlayerInventory) {
    this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
  }

  /**
   * Slot index must be container unique, but not necessarily menu unique
   */
  public WorkbenchMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
    super(T7MenuTypes.INSTANCE.getWORKBENCH().get(), pContainerId, pPlayerInventory);
    this.levelAccess = pAccess;

    this.craftingContainer.addSlots();
    this.wandContainer.addSlots();
    this.playerInventory.addSlots();
    this.resultContainer.addSlots();

    this.addSlotListener(this);
  }

  private void refreshRecipeResult() {
    Level level = this.getPlayer().level();
    CraftingInput craftinginput = this.craftingContainer.asCraftInput();
    var optionalRecipeHolder = level.getRecipeManager().getRecipeFor(T7RecipeTypes.INSTANCE.getWORKBENCH().get(), this.craftingContainer.asCraftInput(), level);

    this.requiredAspects = optionalRecipeHolder.map(r ->
        r.value().assembleAspects()
    ).orElse(new AspectMap());

    if (!level.isClientSide()) {
      var resultItem = optionalRecipeHolder.map(r ->
          r.value().assemble(craftinginput, level.registryAccess())
      ).orElse(ItemStack.EMPTY);

      this.resultContainer.setItem(0, resultItem);
    }

    this.resultContainer.setSlotEnabled(0, this.wandContainer.contains(this.requiredAspects));
  }

  @Override
  protected List<T7Container> getQuickMovePriorities() {
    return List.of(this.wandContainer, this.craftingContainer);
  }

  public WorkbenchResultContainer getResultContainer() {
    return this.resultContainer;
  }

  public WandContainer<WorkbenchMenu> getWandContainer() {
    return this.wandContainer;
  }

  public CraftingContainer3x3 getCraftingContainer() {
    return this.craftingContainer;
  }

  public AspectMap getRequiredAspects() {
    return this.requiredAspects;
  }

  @Override
  public void removed(@NotNull Player pPlayer) {
    super.removed(pPlayer);
    this.levelAccess.execute((level, blockPos) -> this.clearContainer(pPlayer, this.craftingContainer));
    this.levelAccess.execute((level, blockPos) -> this.clearContainer(pPlayer, this.wandContainer));
  }

  @Override
  public boolean stillValid(@NotNull Player pPlayer) {
    return AbstractContainerMenu.stillValid(this.levelAccess, pPlayer, T7Blocks.INSTANCE.getARCANE_WORKBENCH().get());
  }

  @Override
  public void slotChanged(@NotNull AbstractContainerMenu pContainerToSend, int pDataSlotIndex, @NotNull ItemStack pStack) {
    this.refreshRecipeResult();
  }
}
