package me.alegian.thavma.impl.init.registries

import me.alegian.thavma.impl.common.aspect.Aspect
import me.alegian.thavma.impl.common.book.PageType
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.rl
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

object T7Registries {
    val WAND_HANDLE = RegistryBuilder(ResourceKey.createRegistryKey<WandHandleMaterial>(rl("wand_handle")))
        .maxId(Int.MAX_VALUE)
        .create()

    val WAND_CORE = RegistryBuilder(ResourceKey.createRegistryKey<WandCoreMaterial>(rl("wand_core")))
        .maxId(Int.MAX_VALUE)
        .create()

    val ASPECT = RegistryBuilder(ResourceKey.createRegistryKey<Aspect>(rl("aspect")))
        .maxId(Int.MAX_VALUE)
        .create()

    val PAGE_TYPE = RegistryBuilder(ResourceKey.createRegistryKey<PageType<*>>(rl("page_type")))
        .maxId(Int.MAX_VALUE)
        .create()
}
