package me.alegian.thavma.impl.client.renderer.blockentity;

import me.alegian.thavma.impl.client.renderer.geo.layer.EmissiveGeoLayer;
import me.alegian.thavma.impl.common.block.entity.MatrixBE;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import static me.alegian.thavma.impl.ThavmaKt.rl;

@OnlyIn(Dist.CLIENT)
public class MatrixBER extends GeoBlockRenderer<MatrixBE> {

  public MatrixBER() {
    super(new DefaultedBlockGeoModel<>(rl("infusion_matrix")));
    this.addRenderLayer(new EmissiveGeoLayer<>(this));
  }
}
