package me.alegian.thavma.impl.common.book

import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation

class PageFeatureType<T : PageFeature>(name: ResourceLocation, val codec: MapCodec<T>) {
    val stringName = name.toString()

    override fun toString(): String {
        return stringName
    }
}