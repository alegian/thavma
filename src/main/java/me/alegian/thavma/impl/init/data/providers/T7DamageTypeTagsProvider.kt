package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags.SONIC
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.world.damagesource.DamageTypes
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class T7DamageTypeTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper) : DamageTypeTagsProvider(output, lookupProvider, Thavma.MODID, existingFileHelper) {
  override fun addTags(provider: HolderLookup.Provider) {
    tag(SONIC).add(DamageTypes.SONIC_BOOM)
  }
}
