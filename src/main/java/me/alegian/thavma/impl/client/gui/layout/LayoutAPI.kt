package me.alegian.thavma.impl.client.gui.layout

import net.minecraft.world.phys.Vec2

/**
 * A Layout System for creating Component-based GUIs.
 *
 * LayoutInternals.kt contains the implementation details,
 * not needed for usage.
 */

/**
 * runs an action after the layout of the current element has been calculated
 */
fun afterLayout(callback: T7LayoutElement.() -> Unit) {
  currElement?.afterLayoutCallbacks?.add(callback)
}

enum class Alignment() {
  START,
  CENTER,
  END
}

class Padding(val topLeft: Vec2 = Vec2.ZERO, val bottomRight: Vec2 = Vec2.ZERO)

class Sizing(var x: Size = Size(), var y: Size = Size()) {
  constructor(both: Size = Size()) : this(both, both)
}

class Align(val main: Alignment = Alignment.START, val cross: Alignment = Alignment.START)

fun auto(s: Number = 0f) = Size(SizingMode.AUTO, s.toFloat())
fun fixed(s: Number = 0f) = Size(SizingMode.FIXED, s.toFloat())
fun grow(s: Number = 0f) = Size(SizingMode.GROW, s.toFloat())
fun derived(fn: (Float) -> Float) = Size(SizingMode.FIXED, 0f, fn)

class Props() {
  var width = Size()
  var height = Size()
  var paddingLeft: Number = 0f
  var paddingRight: Number = 0f
  var paddingTop: Number = 0f
  var paddingBottom: Number = 0f
  var gap: Number = 0f
  var alignMain = Alignment.START
  var alignCross = Alignment.START

  var size: Size
    get() = throw UnsupportedOperationException()
    set(value) {
      width = value
      height = value
    }

  var paddingX: Number
    get() = throw UnsupportedOperationException()
    set(value) {
      paddingLeft = value
      paddingRight = value
    }

  var paddingY: Number
    get() = throw UnsupportedOperationException()
    set(value) {
      paddingTop = value
      paddingBottom = value
    }

  var padding: Number
    get() = throw UnsupportedOperationException()
    set(value) {
      paddingX = value
      paddingY = value
    }

  var align: Alignment
    get() = throw UnsupportedOperationException()
    set(value) {
      alignMain = value
      alignCross = value
    }

  internal fun buildElement(direction: Direction, children: T7LayoutElement.() -> Unit) =
    createElement(
      Sizing(width, height),
      Padding(Vec2(paddingLeft.toFloat(), paddingTop.toFloat()), Vec2(paddingRight.toFloat(), paddingBottom.toFloat())),
      direction,
      gap.toFloat(),
      Align(alignMain, alignCross),
      children
    )
}

fun Row(
  propSetter: Props.() -> Unit = {},
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val props = Props()
  props.propSetter()
  return props.buildElement(Direction.LEFT_RIGHT, children)
}

fun Column(
  propSetter: Props.() -> Unit = {},
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val props = Props()
  props.propSetter()
  return props.buildElement(Direction.TOP_BOTTOM, children)
}

fun Box(
  propSetter: Props.() -> Unit = {},
  children: T7LayoutElement.() -> Unit
): T7LayoutElement {
  val props = Props()
  props.propSetter()
  return props.buildElement(Direction.NONE, children)
}