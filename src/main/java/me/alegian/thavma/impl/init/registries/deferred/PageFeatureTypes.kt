package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.book.PageFeatureType
import me.alegian.thavma.impl.common.book.ParagraphFeature
import me.alegian.thavma.impl.common.book.TitleFeature
import me.alegian.thavma.impl.init.registries.T7Registries
import me.alegian.thavma.impl.rl
import net.neoforged.neoforge.registries.DeferredRegister

object PageFeatureTypes {
    val REGISTRAR = DeferredRegister.create(T7Registries.PAGE_FEATURE_TYPE.key(), Thavma.MODID)

    val PARAGRAPH = REGISTRAR.register("paragraph") { -> PageFeatureType<ParagraphFeature>(rl("paragraph"), ParagraphFeature.CODEC)}
    val TITLE = REGISTRAR.register("title") { -> PageFeatureType<TitleFeature>(rl("paragraph"), TitleFeature.CODEC)}

}