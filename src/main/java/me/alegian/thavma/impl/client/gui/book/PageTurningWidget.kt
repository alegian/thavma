package me.alegian.thavma.impl.client.gui.book

import com.mojang.blaze3d.systems.RenderSystem
import me.alegian.thavma.impl.client.gui.research_table.ButtonWidget
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.translateXY
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.block.ResearchTableBlock
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2

class PageTurningWidget(position: Vec2, private val pointsRight: Boolean, private val handleClick: () -> Unit) :
    AbstractWidget(position.x.toInt(), position.y.toInt(), LEFT_TEXTURE.width, LEFT_TEXTURE.height, Component.translatable(if (pointsRight) rightTranslationId else leftTranslationId)) {

    init {
        tooltip = Tooltip.create(message)
    }

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        guiGraphics.usePose {
            translateXY(x, y)
            val texture = if (pointsRight) ButtonWidget.RIGHT_TEXTURE else ButtonWidget.LEFT_TEXTURE
            guiGraphics.blit(texture)
        }
    }

    override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
        handleClick()
    }

    companion object {
        val LEFT_TEXTURE = ButtonWidget.LEFT_TEXTURE
        val RIGHT_TEXTURE = ButtonWidget.RIGHT_TEXTURE
        private val namespace = ".buttonWidget"
        val leftTranslationId = ResearchTableBlock.CONTAINER_TITLE + namespace + ".left"
        val rightTranslationId = ResearchTableBlock.CONTAINER_TITLE + namespace + ".right"
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
    }
}