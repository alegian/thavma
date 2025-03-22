package me.alegian.thavma.impl.client.screen

import net.minecraft.client.gui.components.Renderable
import kotlin.math.max

private var currParent: T7LayoutElement? = null

enum class Axis {
  VERTICAL,
  HORIZONTAL;

  fun cross() = if (this == VERTICAL) HORIZONTAL else VERTICAL
}

enum class Direction(val axis: Axis? = null) {
  NONE, LEFT_RIGHT(Axis.HORIZONTAL), TOP_BOTTOM(Axis.VERTICAL)
}

private fun T7Layout(
  position: Position,
  size: Size,
  padding: Padding,
  direction: Direction,
  gap: Int,
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val element = T7LayoutElement(position, size, padding, direction, gap)

  currParent = element
  element.children()
  currParent = element.parent

  element.calculateSizes()
  if (element.parent == null) {
    element.calculatePositionsRecursively()
    currParent = null
  }
  return element
}

class Position(var x: Int = 0, var y: Int = 0)
class Size(var width: Int = 0, var height: Int = 0)
class Padding(val left: Int = 0, val right: Int = 0, val top: Int = 0, val bottom: Int = 0) {
  constructor(all: Int) : this(all, all, all, all)
  constructor(x: Int, y: Int) : this(x, x, y, y)
}

class T7LayoutElement(
  var position: Position,
  var size: Size,
  val padding: Padding,
  val direction: Direction,
  val gap: Int,
) {
  val children = mutableListOf<T7LayoutElement>()
  val parent = currParent

  init {
    parent?.children?.add(this)
  }

  // first pass: sizing
  fun calculateSizes() {
    size.width += padding.left + padding.right
    size.height += padding.top + padding.bottom
    val childGaps = gap * (children.size - 1)
    addSizeAlong(direction.axis, childGaps)

    if (parent == null) return
    val parentAxis = parent.direction.axis
    parent.addSizeAlong(parentAxis, getSizeAlong(parentAxis))
    parent.maxSizeAlong(parentAxis?.cross(), getSizeAlong(parentAxis?.cross()))
  }

  // second pass: positioning
  // TODO: support reverse directions
  fun calculatePositionsRecursively() {
    var xOffset = position.x + padding.left
    var yOffset = position.y + padding.top

    for (child in children) {
      child.position.x = xOffset
      child.position.y = yOffset
      if (direction == Direction.LEFT_RIGHT)
        xOffset += child.size.width + gap
      else if (direction == Direction.TOP_BOTTOM)
        yOffset += child.size.height + gap

      child.calculatePositionsRecursively()
    }
  }

  // helper
  fun addSizeAlong(axis: Axis?, dx: Int) {
    if (axis == Axis.HORIZONTAL) {
      size.width += dx
    } else if (axis == Axis.VERTICAL) {
      size.height += dx
    }
  }

  // helper
  fun maxSizeAlong(axis: Axis?, dx: Int) {
    if (axis == Axis.HORIZONTAL) {
      size.width = max(size.width, dx)
    } else if (axis == Axis.VERTICAL) {
      size.height = max(size.height, dx)
    }
  }

  // helper
  fun getSizeAlong(axis: Axis?): Int {
    if (axis == Axis.HORIZONTAL) {
      return size.width
    } else if (axis == Axis.VERTICAL) {
      return size.height
    }
    return 0
  }

  // helper
  fun debugRect(color: Int) = Renderable { guiGraphics, _, _, _ ->
    guiGraphics.fill(position.x, position.y, position.x + size.width, position.y + size.height, color)
  }
}

fun Column(
  position: Position = Position(),
  size: Size = Size(),
  padding: Padding = Padding(),
  gap: Int = 0,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.TOP_BOTTOM, gap, children)

fun Row(
  position: Position = Position(),
  size: Size = Size(),
  padding: Padding = Padding(),
  gap: Int = 0,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.LEFT_RIGHT, gap, children)

fun Box(
  position: Position = Position(),
  size: Size = Size(),
  padding: Padding = Padding(),
  gap: Int = 0,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.NONE, gap, children)