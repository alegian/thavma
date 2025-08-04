package me.alegian.thavma.impl.client.gui.book

import me.alegian.thavma.impl.client.gui.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.drawCenteredString
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.book.CraftingPage
import me.alegian.thavma.impl.common.book.TextPage
import me.alegian.thavma.impl.common.recipe.translationId
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.Renderable
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.RecipeType
import kotlin.jvm.optionals.getOrNull

object CraftingPageRenderer : PageRenderer<CraftingPage> {
  private val GRID = Texture("gui/book/crafting", 96, 96, 96, 96)
  private val TITLE = Component.translatable(RecipeType.CRAFTING.translationId)

  override fun initPage(screen: EntryScreen, page: CraftingPage) {
    val recipe = Minecraft.getInstance().level?.recipeManager?.byKey(page.recipeRL)?.getOrNull()?.value
    if (recipe !is CraftingRecipe) return

    Column({
      size = grow()
      gap = 4
    }) {
      Title(screen)

      Row({
        width = grow()
        alignMain = Alignment.CENTER
      }) {
        Row({
          width = fixed(GRID.width)
          height = fixed(GRID.height)
        }) {
          afterLayout {
            screen.addRenderableOnly(texture(GRID))
          }
        }
      }
    }
  }

  private fun Title(screen: EntryScreen) {
    Row({
      width = grow()
      height = fixed(screen.getFont().lineHeight)
    }) {
      afterLayout {
        screen.addRenderableOnly(Renderable { guiGraphics, mouseX, mouseY, tickDelta ->
          guiGraphics.usePose {
            translateXY(position.x, position.y)
            guiGraphics.drawCenteredString(screen.getFont(), TITLE, size.x / 2)
          }
        })
      }
    }
  }
}
