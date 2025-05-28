package me.alegian.thavma.impl.client.renderer.blockentity

import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.common.block.entity.PillarBE
import me.alegian.thavma.impl.rl
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class PillarBER : GeoBlockRenderer<PillarBE>(DefaultedBlockGeoModel(rl("infusion_pillar"))) {
  init {
    addRenderLayer(EmissiveGeoLayer(this))
  }
}
