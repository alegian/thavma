package me.alegian.thavma.impl.init.data.providers;

import me.alegian.thavma.impl.init.registries.deferred.T7ParticleTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class T7ParticleDescriptionProvider extends ParticleDescriptionProvider {
  public T7ParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, fileHelper);
  }

  @Override
  protected void addDescriptions() {
    this.spriteSet(T7ParticleTypes.INSTANCE.getCRUCIBLE_BUBBLE().get(), ResourceLocation.withDefaultNamespace("bubble"));
  }
}
