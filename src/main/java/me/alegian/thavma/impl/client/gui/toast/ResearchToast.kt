package me.alegian.thavma.impl.client.gui.toast

import me.alegian.thavma.impl.client.clientRegistry
import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.T7DatapackRegistries
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.components.toasts.ToastComponent
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.DyeColor

private val BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/advancement")
private const val DISPLAY_TIME = 5000

class ResearchToast(key: ResourceKey<ResearchEntry>) : Toast {
  val researchEntry = clientRegistry(T7DatapackRegistries.RESEARCH_ENTRY)?.get(key)

  override fun render(guiGraphics: GuiGraphics, toastComponent: ToastComponent, timeSinceLastVisible: Long): Toast.Visibility {
    if (researchEntry == null) return Toast.Visibility.HIDE

    val font = toastComponent.minecraft.font
    val title = researchEntry.title
    val header = Component.translatable(ResearchEntry.TOAST_TRANSLATION)
    val icon = researchEntry.icon

    guiGraphics.blitSprite(BACKGROUND_SPRITE, 0, 0, width(), height())

    val yellowNoAlpha = DyeColor.YELLOW.textColor or 0xFF000000.toInt()
    val whiteNoAlpha = -1

    guiGraphics.drawString(font, header, 30, 7, yellowNoAlpha, false)
    guiGraphics.drawString(font, title, 30, 18, whiteNoAlpha, false)

    guiGraphics.renderFakeItem(icon, 8, 8)

    return if (timeSinceLastVisible >= DISPLAY_TIME * toastComponent.notificationDisplayTimeMultiplier)
      Toast.Visibility.HIDE
    else
      Toast.Visibility.SHOW
  }
}

