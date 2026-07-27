package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.common.book.RecipeFeature
import me.alegian.thavma.impl.common.recipe.translationId
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeType
import kotlin.jvm.optionals.getOrNull

object RecipeFeatureRenderer : PageFeatureRenderer<RecipeFeature> {
  private val GRID = Texture("gui/book/crafting", 96, 96, 96, 96)
  private val RESULT = Texture("gui/book/result", 32, 32, 32, 32)
  private val TITLE = Component.translatable(RecipeType.CRAFTING.translationId)
  private const val GAP = 12

  override fun initPageFeature(screen: EntryScreen, feature: RecipeFeature, font: Font) {
    val recipe = Minecraft.getInstance().level?.recipeManager?.byKey(feature.recipeRL)?.getOrNull()?.value
    if (recipe !is CraftingRecipe) return // TODO: support other recipe types

    Column({
      alignCross = Alignment.CENTER
      size = grow()
      gap = GAP
    }) {
      Title()

      TextureBox(RESULT) {}

      TextureBox(GRID) {}
    }
  }

  private fun Title() {
    val font = Minecraft.getInstance().font

    Row({
      height = fixed(font.lineHeight)
    }) {
      draw {
        Renderable { guiGraphics, _, _, _ ->
          guiGraphics.drawCenteredString(font, TITLE, size.x / 2)
        }
      }
    }
  }
}
