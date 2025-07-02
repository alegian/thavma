package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.FluidTagsProvider
import net.minecraft.tags.FluidTags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class T7FluidTagProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper) : FluidTagsProvider(output, lookupProvider, Thavma.MODID, existingFileHelper) {
  override fun addTags(pProvider: HolderLookup.Provider) {
    tag(T7Tags.Fluids.CRUCIBLE_HEAT_SOURCES).addTag(FluidTags.LAVA)
  }
}
