package me.alegian.thavma.impl.common.menu.slot

import me.alegian.thavma.impl.common.menu.Menu

/**
 * Used for adding size to slots, allowing them to be dynamically positioned
 */
interface DynamicSlot<T : Menu> {
  var actualX: Float

  var actualY: Float

  var size: Int

  val menu: T
}
