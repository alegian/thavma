package me.alegian.thavma.impl.client.event

import me.alegian.thavma.impl.client.renderer.geo.WandRenderer
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

class RegisterFocusModelsEvent : Event(), IModBusEvent{
  fun register(item: ItemLike, modelRL: ResourceLocation) {
    WandRenderer.FOCUS_MODELS[item.asItem()] = ModelResourceLocation.standalone(modelRL)
  }
}