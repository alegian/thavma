package me.alegian.thavma.impl.client.gui.layout

/**
 * A Layout System for creating Component-based GUIs.
 *
 * LayoutInternals.kt contains the implementation details,
 * not needed for usage.
 */

enum class Alignment() {
  START,
  CENTER,
  END
}

class Padding(var left: Float = 0f, var right: Float = 0f, var top: Float = 0f, var bottom: Float = 0f)

class Sizing(var x: Size = Size(), var y: Size = Size()) {
  constructor(both: Size = Size()) : this(both, both)
}

class Align(val main: Alignment = Alignment.START, val cross: Alignment = Alignment.START)

fun auto(s: Number = 0f) = Size(SizingMode.AUTO, s.toFloat())
fun fixed(s: Number = 0f) = Size(SizingMode.FIXED, s.toFloat())
fun grow(s: Number = 0f) = Size(SizingMode.GROW, s.toFloat())

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
      Padding(paddingLeft.toFloat(), paddingRight.toFloat(), paddingTop.toFloat(), paddingBottom.toFloat()),
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