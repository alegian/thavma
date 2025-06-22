package me.alegian.thavma.impl.client.renderer.blockentity

import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer
import me.alegian.thavma.impl.common.block.entity.WorkbenchBE
import me.alegian.thavma.impl.rl
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class WorkbenchBER : GeoBlockRenderer<WorkbenchBE>(DefaultedBlockGeoModel(rl("arcane_workbench"))){
  init {
    addRenderLayer(EmissiveGeoLayer(this))
  }
}
