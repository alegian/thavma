package me.alegian.thavma.impl.client.renderer.blockentity;

import me.alegian.thavma.impl.common.block.entity.WorkbenchBE;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import static me.alegian.thavma.impl.ThavmaKt.rl;

@OnlyIn(Dist.CLIENT)
public class WorkbenchBER extends GeoBlockRenderer<WorkbenchBE> {
  public WorkbenchBER() {
    super(new DefaultedBlockGeoModel<>(rl("arcane_workbench")));
  }
}
