package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.common.util.dot
import net.minecraft.client.gui.components.Renderable
import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.deepCopy
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.times
import kotlin.math.max

private var currParent: T7LayoutElement? = null

enum class Axis(val basis: Vec2) {
  NONE(Vec2.ZERO),
  VERTICAL(Vec2(0f, 1f)),
  HORIZONTAL(Vec2(1f, 0f));

  fun cross(): Axis {
    return if (this == VERTICAL) HORIZONTAL
    else if (this == HORIZONTAL) VERTICAL
    else throw UnsupportedOperationException()
  }
}

enum class Direction(val axis: Axis) {
  NONE(Axis.NONE), LEFT_RIGHT(Axis.HORIZONTAL), TOP_BOTTOM(Axis.VERTICAL)
}

private fun T7Layout(
  position: Vec2,
  size: Vec2,
  padding: Padding,
  direction: Direction,
  gap: Float,
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

class Padding(val left: Float = 0f, val right: Float = 0f, val top: Float = 0f, val bottom: Float = 0f) {
  constructor(all: Float) : this(all, all, all, all)
  constructor(x: Float, y: Float) : this(x, x, y, y)

  fun start(direction: Direction): Vec2 {
    if (direction == Direction.LEFT_RIGHT || direction == Direction.TOP_BOTTOM) return Vec2(left, top)
    return Vec2(0f, 0f)
  }

  fun end(direction: Direction): Vec2 {
    if (direction == Direction.LEFT_RIGHT || direction == Direction.TOP_BOTTOM) return Vec2(right, bottom)
    return Vec2(0f, 0f)
  }

  val all = Vec2(left + right, top + bottom)
}

class T7LayoutElement(
  var position: Vec2,
  var size: Vec2,
  val padding: Padding,
  val direction: Direction,
  val gap: Float,
) {
  val children = mutableListOf<T7LayoutElement>()
  val parent = currParent

  init {
    parent?.children?.add(this)
  }

  // first pass
  fun calculateInitialSizes() {
    size = size + padding.all
    val childGaps = gap * (children.size - 1)
    size = size + direction.axis.basis * childGaps

    if (parent == null) return
    val parentAxis = parent.direction.axis
    parent.size += parentAxis.basis * (parentAxis.basis dot size)
    val crossBasis = parentAxis.cross().basis
    parent.size = max(parent.size, crossBasis * (size dot crossBasis))
  }

  // second pass
  fun calculateDynamicSizesRecursively() {
    var remainingSize = size - padding.all

    for (child in children) {
      val directionBasis = direction.axis.basis
      remainingSize = remainingSize - directionBasis * (child.size dot directionBasis)
    }
  }

  // third pass
  fun calculatePositionsRecursively() {
    var offset = position + padding.start(direction)

    for (child in children) {
      child.position = offset.deepCopy()

      val directionBasis = direction.axis.basis
      offset += directionBasis * (gap + (child.size dot directionBasis))

      child.calculatePositionsRecursively()
    }
  }

  // helper
  fun debugRect(color: Int) = Renderable { guiGraphics, _, _, _ ->
    guiGraphics.fill(position.x.toInt(), position.y.toInt(), position.x.toInt() + size.x.toInt(), position.y.toInt() + size.y.toInt(), color)
  }
}

fun Column(
  position: Vec2 = Vec2.ZERO,
  size: Vec2 = Vec2.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.TOP_BOTTOM, gap, children)

fun Row(
  position: Vec2 = Vec2.ZERO,
  size: Vec2 = Vec2.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.LEFT_RIGHT, gap, children)

fun Box(
  position: Vec2 = Vec2.ZERO,
  size: Vec2 = Vec2.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, size, padding, Direction.NONE, gap, children)

fun max(a: Vec2, b: Vec2) = Vec2(max(a.x, b.x), max(a.y, b.y))