package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.PageType

val PAGE_RENDERERS = mutableMapOf<PageType<*>, PageRenderer<*>>()

interface PageRenderer<T : Page> {
  fun initPage(screen: EntryScreen, page: T)
}