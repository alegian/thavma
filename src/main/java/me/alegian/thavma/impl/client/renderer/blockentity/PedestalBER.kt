package me.alegian.thavma.impl.client.renderer.blockentity

import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.client.renderer.geo.layer.ItemRenderLayer
import me.alegian.thavma.impl.common.block.entity.PedestalBE
import me.alegian.thavma.impl.rl
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer


class PedestalBER : GeoBlockRenderer<PedestalBE>(DefaultedBlockGeoModel(rl("infusion_pedestal"))) {
  init {
    addRenderLayer(EmissiveGeoLayer(this))
    addRenderLayer(ItemRenderLayer(this, 1f))
  }
}
