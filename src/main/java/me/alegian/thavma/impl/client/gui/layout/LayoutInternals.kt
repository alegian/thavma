package me.alegian.thavma.impl.client.gui.layout

import me.alegian.thavma.impl.common.util.div
import me.alegian.thavma.impl.common.util.minus
import me.alegian.thavma.impl.common.util.plus
import me.alegian.thavma.impl.common.util.times
import net.minecraft.world.phys.Vec2
import kotlin.math.max
import kotlin.math.round

/**
 * A Layout System for creating Component-based GUIs.
 * Inspired by Android Jetpack Compose, and Nic Barker's excellent
 * video on UI algorithms (see README)
 *
 * See LayoutAPI.kt for usable components.
 */

internal var currElement: T7LayoutElement? = null
private fun max(a: Vec2, b: Vec2) = Vec2(max(a.x, b.x), max(a.y, b.y))
private val xAxis = Vec2(1f, 0f)
private val yAxis = Vec2(0f, 1f)

internal enum class Direction(val basis: Vec2) {
  NONE(Vec2.ZERO),
  LEFT_RIGHT(Vec2(1f, 0f)),
  TOP_BOTTOM(Vec2(0f, 1f));

  val crossBasis = Vec2(basis.y, basis.x)

  val opposite: Direction
    get() =
      if (this == LEFT_RIGHT) TOP_BOTTOM
      else if (this == TOP_BOTTOM) LEFT_RIGHT
      else NONE
}

internal enum class SizingMode {
  AUTO,
  FIXED,
  GROW
}

class Size internal constructor(
  internal val mode: SizingMode = SizingMode.AUTO,
  internal var value: Number = 0f,
  internal val fromWidth: ((Float) -> Float)? = null,
)

// when adding children, negative sign means "move left"
private val Alignment.sign: Float
  get() = when (this) {
    Alignment.START -> 1f
    Alignment.CENTER -> 1f
    Alignment.END -> -1f
  }

// where to start placing children (relative to parent available space)
private val Alignment.factor: Float
  get() = when (this) {
    Alignment.START -> 0f
    Alignment.CENTER -> 0.5f
    Alignment.END -> 1f
  }

private fun Align.signs(direction: Direction): Vec2 {
  return direction.basis * main.sign + direction.crossBasis * cross.sign
}

private val Padding.all: Vec2
  get() = topLeft + bottomRight

internal fun createElement(
  sizing: Sizing,
  padding: Padding,
  direction: Direction,
  gap: Float,
  align: Align,
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val element = T7LayoutElement(sizing, padding, direction, gap, align)

  currElement = element
  element.children()
  currElement = element.parent

  if (element.parent == null) {
    // first pass: reverse BFS
    element.calculateInitialSizesRecursively(xAxis)
    // second pass: DFS from root
    element.calculateDynamicSizesRecursively(xAxis)
    // third pass: reverse BFS (resolves derived heights)
    element.calculateInitialSizesRecursively(yAxis)
    // fourth pass: DFS from root
    element.calculateDynamicSizesRecursively(yAxis)
    // fifth pass: DFS from root
    element.calculatePositionsRecursively()
    // sixth pass: DFS from root
    element.afterLayoutRecursively()
    currElement = null
  }
  return element
}

class T7LayoutElement internal constructor(
  internal val sizing: Sizing,
  internal val padding: Padding,
  internal val direction: Direction,
  internal val gap: Float,
  internal val align: Align,
) {
  var position = Vec2.ZERO
  val children = mutableListOf<T7LayoutElement>()
  val parent = currElement
  var size = Vec2(sizing.x.value.toFloat(), sizing.y.value.toFloat())
  internal val growBasis: Vec2
    get() {
      var growX = 0f
      var growY = 0f
      if (sizing.x.mode == SizingMode.GROW) growX = 1f
      if (sizing.y.mode == SizingMode.GROW) growY = 1f
      return Vec2(growX, growY)
    }
  internal val fixedMask: Vec2
    get() {
      var maskX = 1f
      var maskY = 1f
      if (sizing.x.mode == SizingMode.FIXED) maskX = 0f
      if (sizing.y.mode == SizingMode.FIXED) maskY = 0f
      return Vec2(maskX, maskY)
    }
  internal var afterLayoutCallbacks = mutableListOf<T7LayoutElement.() -> Unit>()

  init {
    parent?.children?.add(this)
  }

  /**
   * calculates sizes for each element along one axis, in reverse BFS order (post-order DFS),
   * based on paddings, gaps and sizes of children. For the y-axis pass, also resolves derived heights.
   */
  internal fun calculateInitialSizesRecursively(axis: Vec2) {
    for (child in children)
      child.calculateInitialSizesRecursively(axis)

    if (axis.y != 0f) sizing.y.fromWidth?.let { size = Vec2(size.x, it(size.x)) }

    size += padding.all * axis
    size += direction.basis * (gap * (children.size - 1)) * axis

    if (parent == null) return
    parent.size += size * parent.direction.basis * axis * parent.fixedMask
    parent.size = max(parent.size, size * parent.direction.crossBasis * axis * parent.fixedMask)
  }

  /**
   * calculates the amount by which elements with "grow" should be expanded along one axis,
   * ran recursively from the root (DFS)
   */
  internal fun calculateDynamicSizesRecursively(axis: Vec2) {
    val mainBasis = direction.basis
    val crossBasis = direction.crossBasis
    var remainingSize = size - padding.all - mainBasis * (gap * (children.size - 1))

    if (mainBasis.dot(axis) != 0f) {
      val mainGrowables = mutableListOf<T7LayoutElement>()
      for (child in children) {
        val canGrow = child.growBasis.dot(mainBasis) != 0f
        if (canGrow) mainGrowables.add(child)
        else remainingSize -= child.size * mainBasis
      }
      for (child in mainGrowables)
        child.size = (remainingSize / mainGrowables.size.toFloat()) * mainBasis + child.size * crossBasis
    }

    if (crossBasis.dot(axis) != 0f) {
      for (child in children) {
        val canGrow = child.growBasis.dot(crossBasis) != 0f
        if (!canGrow) continue
        child.size += (remainingSize - child.size) * crossBasis
      }
    }

    for (child in children)
      child.calculateDynamicSizesRecursively(axis)
  }

  /**
   * calculates the final position of each element,
   * using paddings, gaps and sizes of children. Ran recursively from the root (DFS)
   */
  internal fun calculatePositionsRecursively() {
    val childPosition = position + (paddingStart() * align.signs(direction))

    val childrenLength = children.map { c -> c.size.dot(direction.basis) }.sum()
    val remainingMain = (size - padding.all).dot(direction.basis) - childrenLength

    var mainOffset = round(remainingMain * align.main.factor)

    for (child in children) {
      val remainingCross = (size - padding.all - child.size).dot(direction.crossBasis)
      val crossOffset = round(remainingCross * align.cross.factor)

      child.position = childPosition + (direction.basis * mainOffset + direction.crossBasis * crossOffset)

      mainOffset += (gap + (child.size.dot(direction.basis))) * align.main.sign

      child.calculatePositionsRecursively()
    }
  }

  /**
   * side effects after layout (e.g. drawing)
   */
  internal fun afterLayoutRecursively() {
    for (callback in afterLayoutCallbacks)
      callback()
    for (child in children)
      child.afterLayoutRecursively()
  }

  private fun T7LayoutElement.paddingStart(): Vec2 {
    val mainVec = if (align.main == Alignment.START) padding.topLeft else padding.bottomRight
    val crossVec = if (align.cross == Alignment.START) padding.topLeft else padding.bottomRight
    return mainVec * direction.basis + crossVec * direction.crossBasis
  }
}
