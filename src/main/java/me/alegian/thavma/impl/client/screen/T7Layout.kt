package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.transformOrigin
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.menu.slot.DynamicSlot
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.inventory.Slot
import net.minecraft.world.phys.Vec2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v2d.*
import kotlin.math.max
import kotlin.math.roundToInt

private var currParent: T7LayoutElement? = null
private fun max(a: Vec2, b: Vec2) = Vec2(max(a.x, b.x), max(a.y, b.y))

internal enum class Axis(val basis: Vec2) {
  NONE(Vec2.ZERO),
  VERTICAL(Vec2(0f, 1f)),
  HORIZONTAL(Vec2(1f, 0f));

  fun cross(): Axis {
    return if (this == VERTICAL) HORIZONTAL
    else if (this == HORIZONTAL) VERTICAL
    else throw UnsupportedOperationException()
  }
}

internal enum class Direction(val axis: Axis, val reverse: Boolean? = null) {
  NONE(Axis.NONE),
  LEFT_RIGHT(Axis.HORIZONTAL, false),
  TOP_BOTTOM(Axis.VERTICAL, false),
  RIGHT_LEFT(Axis.HORIZONTAL, true),
  BOTTOM_TOP(Axis.VERTICAL, true);

  val basis = axis.basis
}

internal enum class SizingMode {
  AUTO,
  FIXED,
  GROW
}

class Size internal constructor(internal val mode: SizingMode, internal var value: Float)

class Sizing(var x: Size, var y: Size) {
  constructor(both: Size) : this(both, both)

  companion object {
    internal val ZERO = Sizing(Size(SizingMode.AUTO, 0f), Size(SizingMode.AUTO, 0f))
  }
}

private fun createElement(
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

  internal fun start(direction: Direction): Vec2 {
    if (direction.reverse == false) return Vec2(left, top)
    if (direction.reverse == true) return Vec2(right, bottom)
    return Vec2(0f, 0f)
  }

  internal fun end(direction: Direction): Vec2 {
    if (direction.reverse == false) return Vec2(right, bottom)
    if (direction.reverse == true) return Vec2(left, top)
    return Vec2(0f, 0f)
  }

  val all = Vec2(left + right, top + bottom)
}

class T7LayoutElement internal constructor(
  internal var position: Vec2,
  internal val sizing: Sizing,
  internal val padding: Padding,
  internal val direction: Direction,
  internal val gap: Float
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
    val crossBasis = parent.direction.axis.cross().basis
    parent.size = max(parent.size, size * crossBasis * parent.fixedMask)
  }

  /**
   * second pass: calculates the amount by which elements with "grow"
   * should be expanded, ran recursively from the root (DFS)
   */
  internal fun calculateDynamicSizesRecursively() {
    val mainBasis = direction.basis
    val crossBasis = direction.axis.cross().basis
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

  fun text(content: Component, color: Int = 0) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
    guiGraphics.usePose {
      translate(position.x.toDouble(), position.y.toDouble(), 0.0)
      guiGraphics.drawString(Minecraft.getInstance().font, content, color)
    }
  }

  fun texture(texture: Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
    guiGraphics.usePose {
      translate(position.x.toDouble(), position.y.toDouble(), 0.0)
      guiGraphics.blit(texture)
    }
  }

  fun slotGrid(rows: Int, columns: Int, slots: List<Slot>, getTexture: (Int, Int) -> Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
    guiGraphics.usePose {
      translate(position.x.toDouble(), position.y.toDouble(), 0.0)
      for (i in 0 until rows) {
        pushPose()
        for (j in 0 until columns) {
          guiGraphics.blit(getTexture(i, j))
          val slot = slots[i * columns + j]
          if (slot is DynamicSlot<*>) {
            val pos = transformOrigin()
            slot.actualX = pos.x.roundToInt()
            slot.actualY = pos.y.roundToInt()
            slot.size = getTexture(i, j).width
          }
          translate(getTexture(0, 0).width.toDouble(), 0.0, 0.0)
        }
        popPose()
        translate(0.0, getTexture(0, 0).height.toDouble(), 0.0)
      }
    }
  }

  fun slot(slot: Slot, texture: Texture) = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
    guiGraphics.usePose {
      translate(position.x.toDouble(), position.y.toDouble(), 0.0)
      guiGraphics.blit(texture)
      if (slot is DynamicSlot<*>) {
        val pos = transformOrigin()
        slot.actualX = pos.x.roundToInt()
        slot.actualY = pos.y.roundToInt()
        slot.size = texture.width
      }
    }
  }
}

fun Column(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.TOP_BOTTOM, gap, children)

fun ColumnReverse(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.BOTTOM_TOP, gap, children)

fun Row(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.LEFT_RIGHT, gap, children)

fun RowReverse(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.RIGHT_LEFT, gap, children)

fun Box(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing.ZERO,
  padding: Padding = Padding(),
  gap: Float = 0f,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.NONE, gap, children)

fun auto(s: Float = 0f) = Size(SizingMode.AUTO, s)
fun fixed(s: Float = 0f) = Size(SizingMode.FIXED, s)
fun grow(s: Float = 0f) = Size(SizingMode.GROW, s)