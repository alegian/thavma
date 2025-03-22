package me.alegian.thavma.impl.client.screen

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

enum class Direction(val axis: Axis, val reverse: Boolean? = null) {
  NONE(Axis.NONE),
  LEFT_RIGHT(Axis.HORIZONTAL, false),
  TOP_BOTTOM(Axis.VERTICAL, false),
  RIGHT_LEFT(Axis.HORIZONTAL, true),
  BOTTOM_TOP(Axis.VERTICAL, true);

  val basis = axis.basis
}

enum class SizingMode {
  AUTO,
  FIXED,
  GROW
}

class Size(val mode: SizingMode, var value: Float)

class Sizing(var x: Size, var y: Size) {
  constructor(both: Size) : this(both, both)

  companion object {
    val ZERO = Sizing(Size(SizingMode.AUTO, 0f), Size(SizingMode.AUTO, 0f))
  }
}

private fun T7Layout(
  position: Vec2,
  sizing: Sizing,
  padding: Padding,
  direction: Direction,
  gap: Float,
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val element = T7LayoutElement(position, sizing, padding, direction, gap)

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
    if (direction.reverse == false) return Vec2(left, top)
    if (direction.reverse == true) return Vec2(right, bottom)
    return Vec2(0f, 0f)
  }

  fun end(direction: Direction): Vec2 {
    if (direction.reverse == false) return Vec2(right, bottom)
    if (direction.reverse == true) return Vec2(left, top)
    return Vec2(0f, 0f)
  }

  val all = Vec2(left + right, top + bottom)
}

class T7LayoutElement(
  var position: Vec2,
  val sizing: Sizing,
  val padding: Padding,
  val direction: Direction,
  val gap: Float
) {
  val children = mutableListOf<T7LayoutElement>()
  val parent = currParent
  var size = Vec2(sizing.x.value, sizing.y.value)
  val growBasis: Vec2
    get() {
      if (sizing.x.mode == SizingMode.GROW && sizing.y.mode == SizingMode.GROW) return Vec2(1f, 1f)
      if (sizing.x.mode == SizingMode.GROW) return Vec2(1f, 0f)
      if (sizing.y.mode == SizingMode.GROW) return Vec2(0f, 1f)
      return Vec2.ZERO
    }

  init {
    parent?.children?.add(this)
  }

  // first pass
  fun calculateInitialSizes() {
    size += padding.all
    val childGaps = gap * (children.size - 1)
    size += direction.basis * childGaps

    if (parent == null) return
    val mainBasis = parent.direction.basis
    parent.size += size * mainBasis
    val crossBasis = parent.direction.axis.cross().basis
    parent.size = max(parent.size, (size * crossBasis))
  }

  // second pass
  fun calculateDynamicSizesRecursively() {
    val mainBasis = direction.basis
    val crossBasis = direction.axis.cross().basis
    var remainingSize = size - padding.all - mainBasis * (gap * (children.size - 1))

    for (child in children) {
      remainingSize = remainingSize - (child.size * mainBasis)
    }

    for (child in children) {
      // cross axis remaining size depends on current child
      val actualRemainingSize = remainingSize - (child.size * crossBasis)
      child.size = child.size + child.growBasis * actualRemainingSize
    }
  }

  // third pass
  fun calculatePositionsRecursively() {
    var offset = position + padding.start(direction)

    for (child in children) {
      child.position = offset.deepCopy()

      val directionBasis = direction.basis
      offset += directionBasis * gap + (child.size * directionBasis)

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
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, sizing, padding, Direction.TOP_BOTTOM, gap, children)

fun Row(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, sizing, padding, Direction.LEFT_RIGHT, gap, children)

fun Box(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = T7Layout(position, sizing, padding, Direction.NONE, gap, children)

// TODO: this feels clunky
private fun max(a: Vec2, b: Vec2) = Vec2(max(a.x, b.x), max(a.y, b.y))

fun auto(s: Float = 0f) = Size(SizingMode.AUTO, s)
fun fixed(s: Float = 0f) = Size(SizingMode.FIXED, s)
fun grow(s: Float = 0f) = Size(SizingMode.GROW, s)