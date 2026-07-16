package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.book.*
import me.alegian.thavma.impl.init.registries.T7Registries
import me.alegian.thavma.impl.rl
import net.neoforged.neoforge.registries.DeferredRegister

object PageFeatureTypes {
  val REGISTRAR = DeferredRegister.create(T7Registries.PAGE_FEATURE_TYPE.key(), Thavma.MODID)

  val PARAGRAPH =
    REGISTRAR.register("paragraph") { -> PageFeatureType<ParagraphFeature>(rl("paragraph"), ParagraphFeature.CODEC) }
  val TITLE = REGISTRAR.register("title") { -> PageFeatureType<TitleFeature>(rl("title"), TitleFeature.CODEC) }
  val FIGURE = REGISTRAR.register("figure") { -> PageFeatureType<FigureFeature>(rl("figure"), FigureFeature.CODEC) }
  val RECIPE = REGISTRAR.register("recipe") { -> PageFeatureType<RecipeFeature>(rl("recipe"), RecipeFeature.CODEC) }
  //val FORMCHARSEQ = REGISTRAR.register("formcharseq") { -> PageFeatureType(rl("formcharseq"), FormCharSeqFeature.CODEC) }
}