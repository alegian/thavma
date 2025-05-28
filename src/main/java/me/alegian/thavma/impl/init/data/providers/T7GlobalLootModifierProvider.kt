package me.alegian.thavma.impl.init.data.providers

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.loot.WardenLootModifier
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import java.util.concurrent.CompletableFuture

class T7GlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : GlobalLootModifierProvider(output, registries, Thavma.MODID) {
  override fun start() {
    add("warden", WardenLootModifier(), mutableListOf())
  }
}
