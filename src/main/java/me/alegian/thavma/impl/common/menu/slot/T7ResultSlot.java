package me.alegian.thavma.impl.common.menu.slot;

import me.alegian.thavma.impl.common.menu.Menu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * A Sized ResultSlot, with a back-reference to a Menu
 */
public class T7ResultSlot<T extends Menu> extends ResultSlot implements DynamicSlot<T> {
  private int size = 0;
  private boolean mayPickup = true;
  private final T menu;
  private float x,y;

  public T7ResultSlot(T menu, CraftingContainer craftingContainer, ResultContainer container, int id) {
    super(menu.getPlayer(), craftingContainer, container, id, 0, 0);
    this.menu = menu;
  }

  public @NotNull T getMenu() {
    return this.menu;
  }

  @Override
  public float getActualX() {
    return x;
  }

  @Override
  public float getActualY() {
    return y;
  }

  @Override
  public void setActualX(float x) {
    this.x = x;
  }

  @Override
  public void setActualY(float y) {
    this.y = y;
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public void onTake(Player player, ItemStack stack) {
  }

  public void setMayPickup(boolean mayPickup) {
    this.mayPickup = mayPickup;
  }

  @Override
  public boolean mayPickup(Player player) {
    return this.mayPickup;
  }
}
