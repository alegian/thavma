package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.common.menu.Menu
import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot

/**
 * A Slot that implements Sized, and has a back-reference to a Menu
 */
open class T7Slot<T : Menu>(container: Container, id: Int, override val menu: T) : Slot(container, id, 0, 0), DynamicSlot<T> {
  override var size = 0
  override var actualX = 0f
  override var actualY = 0f
}
