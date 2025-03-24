package me.alegian.thavma.impl.common.menu.slot;

import me.alegian.thavma.impl.common.menu.Menu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

/**
 * A Slot that implements Sized, and has a back-reference to a Menu
 */
public class T7Slot<T extends Menu> extends Slot implements DynamicSlot<T> {
  private int size = 0;
  private final T menu;
  private float x,y;

  public T7Slot(Container container, int id, T menu) {
    super(container, id, 0, 0);
    this.menu = menu;
  }

  @Override
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
}
