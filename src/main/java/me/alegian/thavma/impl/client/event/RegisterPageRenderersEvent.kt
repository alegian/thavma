package me.alegian.thavma.impl.client.event

import me.alegian.thavma.impl.client.gui.book.PAGE_RENDERERS
import me.alegian.thavma.impl.client.gui.book.PageRenderer
import me.alegian.thavma.impl.common.book.Page
import me.alegian.thavma.impl.common.book.PageType
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

class RegisterPageRenderersEvent : Event(), IModBusEvent {
  fun <T: Page> register(pageType:PageType<T>, pageRenderer: PageRenderer<T>) {
    PAGE_RENDERERS[pageType] = pageRenderer
  }
}