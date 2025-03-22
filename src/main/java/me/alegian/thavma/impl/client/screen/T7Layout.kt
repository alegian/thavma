package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.common.util.minusAssign
import me.alegian.thavma.impl.common.util.plusAssign
import net.minecraft.client.gui.components.Renderable
import org.joml.Vector2i
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
  position: Vector2i,
  size: Vector2i,
  padding: Padding,
  direction: Direction,
  gap: Int,
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val element = T7LayoutElement(position, size, padding, direction, gap)

  currParent = element
  element.children()
  currParent = element.parent

  // first pass: reverse BFS
  element.calculateInitialSizes()
  if (element.parent == null) {
    // second pass: DFS from root
    element.calculateDynamicSizesRecursively()
    // third pass: DFS from root
    element.calculatePositionsRecursively()
    currParent = null
  }
  return element
}

class Padding(val left: Int = 0, val right: Int = 0, val top: Int = 0, val bottom: Int = 0) {
  constructor(all: Int) : this(all, all, all, all)
  constructor(x: Int, y: Int) : this(x, x, y, y)

  fun start(direction: Direction): Vector2i {
    if (direction == Direction.LEFT_RIGHT || direction == Direction.TOP_BOTTOM) return Vector2i(left, top)
    return Vector2i(0, 0)
  }

  fun end(direction: Direction): Vector2i {
    if (direction == Direction.LEFT_RIGHT || direction == Direction.TOP_BOTTOM) return Vector2i(right, bottom)
    return Vector2i(0, 0)
  }

  val all = Vector2i(left + right, top + bottom)
}

class T7LayoutElement(
  var position: Vector2i,
  var size: Vector2i,
  val padding: Padding,
  val direction: Direction,
  val gap: Int,
) {
  val children = mutableListOf<T7LayoutElement>()
  val parent = currParent

  init {
    parent?.children?.add(this)
  }

  // first pass
  fun calculateInitialSizes() {
    size += padding.all
    val childGaps = gap * (children.size - 1)
    addSizeAlong(direction.axis, childGaps)

    if (parent == null) return
    val parentAxis = parent.direction.axis
    parent.size += getSizeAlong(parentAxis)
    parent.size = max(parent.size, getSizeAlong(parentAxis?.cross()))
  }

  // second pass
  fun calculateDynamicSizesRecursively() {
    val remainingSize = Vector2i(size)
    remainingSize -= padding.all

    for (child in children) {
      remainingSize -= child.getSizeAlong(direction.axis)
    }
  }

  // third pass
  // TODO: support reverse directions
  fun calculatePositionsRecursively() {
    var xOffset = position.x + padding.left
    var yOffset = position.y + padding.top

    for (child in children) {
      child.position.x = xOffset
      child.position.y = yOffset
      if (direction == Direction.LEFT_RIGHT)
        xOffset += child.size.x + gap
      else if (direction == Direction.TOP_BOTTOM)
        yOffset += child.size.y + gap

      child.calculatePositionsRecursively()
    }
  }

  // helper
  fun addSizeAlong(axis: Axis?, dx: Int) {
    if (axis == Axis.HORIZONTAL) {
      size.x += dx
    } else if (axis == Axis.VERTICAL) {
      size.y += dx
    }
  }

  // helper
  fun getSizeAlong(axis: Axis?): Vector2i {
    if (axis == Axis.HORIZONTAL) {
      return Vector2i(size.x, 0)
    } else if (axis == Axis.VERTICAL) {
      return Vector2i(0, size.y)
    }
    return Vector2i(0, 0)
  }

  // helper
  fun debugRect(color: Int) = Renderable { guiGraphics, _, _, _ ->
    guiGraphics.fill(position.x, position.y, position.x + size.x, position.y + size.y, color)
  }
}

fun Column(
  position: Vector2i = Vector2i(),
  size: Vector2i = Vector2i(),
  padding: Padding = Padding(),
  gap: Int = 0,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.TOP_BOTTOM, gap, children)

fun Row(
  position: Vector2i = Vector2i(),
  size: Vector2i = Vector2i(),
  padding: Padding = Padding(),
  gap: Int = 0,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.LEFT_RIGHT, gap, children)

fun Box(
  position: Vector2i = Vector2i(),
  size: Vector2i = Vector2i(),
  padding: Padding = Padding(),
  gap: Int = 0,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.NONE, gap, children)

fun max(a: Vector2i, b: Vector2i) = Vector2i(max(a.x, b.x), max(a.y, b.y))