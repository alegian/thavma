package me.alegian.thavma.impl.init.data.providers

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import java.util.concurrent.CompletableFuture

class T7LootTableProvider(pOutput: PackOutput, pSubProviders: List<SubProviderEntry>, pRegistries: CompletableFuture<HolderLookup.Provider>) :
  LootTableProvider(pOutput, setOf(), pSubProviders, pRegistries)
