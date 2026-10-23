package me.alegian.thavma.impl.client.event

import me.alegian.thavma.impl.client.gui.book.PAGE_FEATURE_RENDERERS
import me.alegian.thavma.impl.client.gui.book.PageFeatureRenderer
import me.alegian.thavma.impl.common.book.PageFeature
import me.alegian.thavma.impl.common.book.PageFeatureType
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

class RegisterPageFeatureRenderersEvent : Event(), IModBusEvent {
  fun <T : PageFeature> register(pageFeatureType: PageFeatureType<T>, pageFeatureRenderer: PageFeatureRenderer<T>) {
    PAGE_FEATURE_RENDERERS[pageFeatureType] = pageFeatureRenderer
  }
}