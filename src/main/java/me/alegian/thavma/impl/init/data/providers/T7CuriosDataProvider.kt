package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.init.registries.T7Tags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosDataProvider
import java.util.concurrent.CompletableFuture

class T7CuriosDataProvider(output: PackOutput, fileHelper: ExistingFileHelper, lookupProvider: CompletableFuture<HolderLookup.Provider>) : CuriosDataProvider(Thavma.MODID, output, fileHelper, lookupProvider) {
  override fun generate(registries: HolderLookup.Provider, fileHelper: ExistingFileHelper) {
    createEntities(Thavma.MODID + "_player").addPlayer().addSlots(
      T7Tags.Curios.HEAD.location.path,
      T7Tags.Curios.CHARM.location.path
    )
  }
}