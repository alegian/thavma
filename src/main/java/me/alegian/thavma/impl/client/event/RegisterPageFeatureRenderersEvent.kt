package me.alegian.thavma.impl.client.event

import me.alegian.thavma.impl.client.gui.book.PageFeatureRenderer
import me.alegian.thavma.impl.common.book.PageFeature
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

class RegisterPageFeatureRenderersEvent: Event(), IModBusEvent {
  fun register(pageFeatureRenderer: PageFeatureRenderer<PageFeature>) {
    //PAGE_FEATURE_RENDERERS[pageFeatureType] = pageFeatureRenderer
  }
}