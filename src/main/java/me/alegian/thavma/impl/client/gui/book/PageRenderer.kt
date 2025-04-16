package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.PageType
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics

val PAGE_RENDERERS = mutableMapOf<PageType<*>, PageRenderer<*>>()

interface PageRenderer<T : Page> {
  fun render(page: T, guiGraphics: GuiGraphics, font: Font)
}