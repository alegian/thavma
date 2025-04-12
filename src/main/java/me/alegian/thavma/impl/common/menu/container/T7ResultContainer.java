package me.alegian.thavma.impl.common.menu.container;

import me.alegian.thavma.impl.common.menu.Menu;
import me.alegian.thavma.impl.common.menu.slot.SlotRange;
import me.alegian.thavma.impl.common.menu.slot.T7ResultSlot;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import org.jetbrains.annotations.NotNull;

public abstract class T7ResultContainer<T extends Menu> extends ResultContainer implements T7Container {
  protected final T menu;
  protected final CraftingContainer craftingContainer;
  protected final SlotRange.Single range;

  public T7ResultContainer(T menu, CraftingContainer craftingContainer) {
    super();
    this.menu = menu;
    this.craftingContainer = craftingContainer;
    this.range = new SlotRange.Single(menu);
  }

  @Override
  public SlotRange.@NotNull Single getRange() {
    return this.range;
  }

  public T7ResultSlot<?> getSlot(int index) {
    if (this.menu.getSlot(this.range.getStart() + index) instanceof T7ResultSlot<?> slot) return slot;
    throw new IllegalArgumentException("Thavma Exception: T7ResultContainer contains a slot which is not a T7ResultSlot");
  }

  public void setSlotEnabled(int index, boolean enabled) {
    this.getSlot(index).setMayPickup(enabled);
  }
}
