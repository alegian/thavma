package me.alegian.thavma.impl.client.screen.layout

import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.div
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.minus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.times
import kotlin.math.max

private var currParent: T7LayoutElement? = null
private fun max(a: Vec2, b: Vec2) = Vec2(max(a.x, b.x), max(a.y, b.y))

internal enum class Direction(val basis: Vec2) {
  NONE(Vec2.ZERO),
  LEFT_RIGHT(Vec2(1f, 0f)),
  TOP_BOTTOM(Vec2(0f, 1f));

  val crossBasis = Vec2(basis.y, basis.x)
}

internal enum class SizingMode {
  AUTO,
  FIXED,
  GROW
}

class Size internal constructor(internal val mode: SizingMode, internal var value: Float)

internal val Alignment.factor: Float
  get() = when (this) {
    Alignment.START -> 1f
    Alignment.CENTER -> 1f
    Alignment.END -> -1f
  }

internal val Padding.all: Vec2
  get() = Vec2(left + right, top + bottom)

internal fun Padding.start(alignment: Alignment): Vec2 {
  if (alignment == Alignment.START) return Vec2(left, top)
  if (alignment == Alignment.END) return Vec2(right, bottom)
  return Vec2(0f, 0f)
}

internal fun Padding.end(alignment: Alignment): Vec2 {
  if (alignment == Alignment.START) return Vec2(right, bottom)
  if (alignment == Alignment.END) return Vec2(left, top)
  return Vec2(0f, 0f)
}

internal fun createElement(
  position: Vec2,
  sizing: Sizing,
  padding: Padding,
  direction: Direction,
  gap: Float,
  alignment: Alignment,
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val element = T7LayoutElement(position, sizing, padding, direction, gap, alignment)

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

class T7LayoutElement internal constructor(
  internal var position: Vec2,
  internal val sizing: Sizing,
  internal val padding: Padding,
  internal val direction: Direction,
  internal val gap: Float,
  internal val alignment: Alignment
) {
  val children = mutableListOf<T7LayoutElement>()
  val parent = currParent
  var size = Vec2(sizing.x.value, sizing.y.value)
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

  init {
    parent?.children?.add(this)
  }

  /**
   * first pass: calculates sizes for each element, in reverse BFS order,
   * based on paddings, gaps and sizes of children (as determined in first pass)
   */
  internal fun calculateInitialSizes() {
    size += padding.all
    val childGaps = gap * (children.size - 1)
    size += direction.basis * childGaps

    if (parent == null) return
    val mainBasis = parent.direction.basis
    parent.size += size * mainBasis * parent.fixedMask
    val crossBasis = parent.direction.crossBasis
    parent.size = max(parent.size, size * crossBasis * parent.fixedMask)
  }

  /**
   * second pass: calculates the amount by which elements with "grow"
   * should be expanded, ran recursively from the root (DFS)
   */
  internal fun calculateDynamicSizesRecursively() {
    val mainBasis = direction.basis
    val crossBasis = direction.crossBasis
    var remainingSize = size - padding.all - mainBasis * (gap * (children.size - 1))

    // children that can grow along main axis
    val mainGrowable = mutableListOf<T7LayoutElement>()

    for (child in children) {
      remainingSize = remainingSize - (child.size * mainBasis)
      val canGrow = child.growBasis.dot(mainBasis) != 0f
      if (canGrow) mainGrowable.add(child)
    }

    // main axis growth
    for (child in mainGrowable) {
      child.size += ((remainingSize / mainGrowable.size.toFloat()) - child.size) * mainBasis
    }

    // cross axis growth
    for (child in children) {
      val canGrow = child.growBasis.dot(crossBasis) != 0f
      if (!canGrow) continue
      child.size += (remainingSize - child.size) * crossBasis
    }
  }

  /**
   * third pass: calculates the final position of each element,
   * using paddings, gaps and sizes of children (as determined in first and
   * second passes). Ran recursively from the root (DFS)
   */
  internal fun calculatePositionsRecursively() {
    var initialOffset = position + padding.start(alignment)

    val childrenLength = children.map { c -> c.size.dot(direction.basis) }.sum()
    val remainingMain = size.dot(direction.basis) - childrenLength

    var mainOffset = initialOffset.dot(direction.basis)
    if (alignment == Alignment.CENTER) mainOffset += remainingMain / 2f
    else if (alignment == Alignment.END) mainOffset += remainingMain

    for (child in children) {
      var crossOffset = initialOffset.dot(direction.crossBasis)
      val remainingCross = (size - child.size).dot(direction.crossBasis)
      if (alignment == Alignment.CENTER) crossOffset += remainingCross / 2f
      else if (alignment == Alignment.END) crossOffset += remainingCross

      child.position = direction.basis * mainOffset + direction.crossBasis * crossOffset

      mainOffset += (gap + (child.size.dot(direction.basis))) * alignment.factor

      child.calculatePositionsRecursively()
    }
  }
}