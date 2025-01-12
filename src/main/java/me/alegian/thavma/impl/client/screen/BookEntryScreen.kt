package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.blit
import me.alegian.thavma.impl.client.util.drawString
import me.alegian.thavma.impl.client.util.usePose
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

private val BG = Texture("gui/book/background", 510, 282, 512, 512)

class BookEntryScreen : Screen(Component.literal("Book Entry")) {
    private var left = 0.0
    private var top = 0.0
    private val testText = Renderable { guiGraphics: GuiGraphics, _: Int, _: Int, _: Float ->
        guiGraphics.usePose {
            translate(left + 30, top + 30, 0.0)
            guiGraphics.drawString(font, "Reaves likes cockroaches")
        }
    }

    override fun init() {
        super.init()
        left = (width - BG.width) / 2.0
        top = (height - BG.height) / 2.0
        addRenderableOnly(testText)
    }

    override fun renderBackground(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        renderTransparentBackground(guiGraphics)
        guiGraphics.usePose {
            translate(left, top, 0.0)
            guiGraphics.blit(BG)
        }
    }
}
