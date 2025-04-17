package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.PageType
import net.minecraft.client.gui.components.Renderable
import net.minecraft.world.phys.Vec2

val PAGE_RENDERERS = mutableMapOf<PageType<*>, PageRenderer<*>>()

interface PageRenderer<T : Page> {
  fun asRenderable(page: T, position: Vec2, size: Vec2): Renderable
}