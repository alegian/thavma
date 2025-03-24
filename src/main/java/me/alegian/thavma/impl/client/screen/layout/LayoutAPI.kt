package me.alegian.thavma.impl.client.screen.layout

import net.minecraft.world.phys.Vec2

enum class Alignment() {
  START,
  CENTER,
  END
}

class Padding(val left: Float = 0f, val right: Float = 0f, val top: Float = 0f, val bottom: Float = 0f) {
  constructor(all: Float) : this(all, all, all, all)
  constructor(x: Float, y: Float) : this(x, x, y, y)
}

class Sizing(var x: Size = Size(SizingMode.AUTO, 0f), var y: Size = Size(SizingMode.AUTO, 0f)) {
  constructor(both: Size = Size(SizingMode.AUTO, 0f)) : this(both, both)
}

fun auto(s: Float = 0f) = Size(SizingMode.AUTO, s)
fun fixed(s: Float = 0f) = Size(SizingMode.FIXED, s)
fun grow(s: Float = 0f) = Size(SizingMode.GROW, s)

fun Column(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing(),
  padding: Padding = Padding(),
  gap: Float = 0f,
  alignment: Alignment = Alignment.START,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.TOP_BOTTOM, gap, alignment, children)

fun Row(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing(),
  padding: Padding = Padding(),
  gap: Float = 0f,
  alignment: Alignment = Alignment.START,
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.LEFT_RIGHT, gap, alignment, children)

fun Box(
  position: Vec2 = Vec2.ZERO,
  sizing: Sizing = Sizing(),
  padding: Padding = Padding(),
  children: T7LayoutElement.() -> Unit
) = createElement(position, sizing, padding, Direction.NONE, 0f, Alignment.START, children)