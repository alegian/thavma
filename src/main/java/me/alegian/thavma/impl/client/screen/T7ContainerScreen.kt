package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.screen.layout.*
import me.alegian.thavma.impl.client.texture.Texture
import me.alegian.thavma.impl.client.util.usePose
import me.alegian.thavma.impl.common.menu.Menu
import me.alegian.thavma.impl.common.menu.slot.DynamicSlot
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import kotlin.math.min
import kotlin.math.roundToInt

private val INVENTORY_BG = Texture("gui/container/inventory", 174, 97, 256, 256)
private val SLOT_TEXTURE = Texture("gui/container/slot", 18, 18)
const val GAP = 4
private const val INVENTORY_PADDING = 6
private const val HOTBAR_GAP = 4

/**
 * The background texture's size is used to determine the size of the container
 */
abstract class T7ContainerScreen<T : Menu>(menu: T, pPlayerInventory: Inventory, pTitle: Component, private val bgTexture: Texture) : AbstractContainerScreen<T>(menu, pPlayerInventory, pTitle) {
  abstract fun layout()

  override fun init() {
    super.init()
    topPos = 0
    leftPos = 0

    Row({
      width = fixed(this@T7ContainerScreen.width)
      height = fixed(this@T7ContainerScreen.height)
      align = Alignment.CENTER
    }) {
      Column({
        gap = GAP
        alignCross = Alignment.CENTER
      }) {
        Box({
          height = fixed(font.lineHeight)
          width = grow()
        }) {
          addRenderableOnly(text(this@T7ContainerScreen.title, 0x83FF9B))
        }

        TextureBox(bgTexture) {
          layout()
        }

        TextureBox(INVENTORY_BG) {
          Column({
            size = grow()
            paddingX = INVENTORY_PADDING
            paddingTop = INVENTORY_PADDING / 2
            gap = INVENTORY_PADDING / 2
          }) {
            Box({
              width = grow()
              height = fixed(font.lineHeight)
            }) {
              addRenderableOnly(text(this@T7ContainerScreen.playerInventoryTitle, 0x404040))
            }

            Column({
              gap = HOTBAR_GAP
            }) {
              Box({
                height = fixed(SLOT_TEXTURE.height * 3)
                width = grow()
              }) {
                addRenderableOnly(slotGrid(3, 9, menu.playerInventory.range.slots) { _, _ -> SLOT_TEXTURE })
              }

              Box({
                height = fixed(SLOT_TEXTURE.height)
                width = grow()
              }) {
                addRenderableOnly(slotGrid(1, 9, menu.playerInventory.range.slots.takeLast(9)) { _, _ -> SLOT_TEXTURE })
              }
            }
          }
        }
      }
    }
  }

  override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
    super.render(guiGraphics, mouseX, mouseY, partialTick)
    renderTooltip(guiGraphics, mouseX, mouseY)
  }

  override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {}

  override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {}

  override fun renderSlot(guiGraphics: GuiGraphics, slot: Slot) {
    if (slot !is DynamicSlot<*>) return super.renderSlot(guiGraphics, slot)

    val padding = (slot.size - 16) / 2
    var itemStack = slot.item
    var quickReplace = false
    var drawItem = slot === this.clickedSlot && !draggingItem.isEmpty && !this.isSplittingStack
    val carriedStack = menu.carried
    var count: String? = null
    if (slot === this.clickedSlot && !draggingItem.isEmpty && this.isSplittingStack && !itemStack.isEmpty) {
      itemStack = itemStack.copyWithCount(itemStack.count / 2)
    } else if (this.isQuickCrafting && quickCraftSlots.contains(slot) && !carriedStack.isEmpty) {
      if (quickCraftSlots.size == 1) {
        return
      }

      if (AbstractContainerMenu.canItemQuickReplace(slot, carriedStack, true) && menu.canDragTo(slot)) {
        quickReplace = true
        val k = min(carriedStack.maxStackSize.toDouble(), slot.getMaxStackSize(carriedStack).toDouble()).toInt()
        val l = if (slot.item.isEmpty) 0 else slot.item.count
        var i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, carriedStack) + l
        if (i1 > k) {
          i1 = k
          count = ChatFormatting.YELLOW.toString() + k
        }

        itemStack = carriedStack.copyWithCount(i1)
      } else {
        quickCraftSlots.remove(slot)
        this.recalculateQuickCraftRemaining()
      }
    }

    guiGraphics.usePose {
      translate(slot.actualX + padding, slot.actualY + padding, 100.0f)
      if (itemStack.isEmpty && slot.isActive) {
        val pair = slot.noItemIcon
        if (pair != null) {
          val sprite = Minecraft.getInstance().getTextureAtlas(pair.first).apply(pair.second)
          guiGraphics.blit(0, 0, 0, 16, 16, sprite)
          drawItem = true
        }
      }

      if (!drawItem) {
        if (quickReplace) {
          guiGraphics.fill(0, 0, 16, 16, -2130706433)
        }

        renderSlotContents(guiGraphics, itemStack, slot, count)
      }
    }
  }

  override fun renderSlotHighlight(guiGraphics: GuiGraphics, slot: Slot, mouseX: Int, mouseY: Int, partialTick: Float) {
    if (slot !is DynamicSlot<*>) return super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick)

    if (slot.isHighlightable) {
      val color = getSlotColor(slot.index)
      val padding = (slot.size - 16) / 2
      guiGraphics.usePose {
        translate(slot.actualX, slot.actualY, 0f)
        guiGraphics.fillGradient(
          RenderType.guiOverlay(),
          padding, padding,
          padding + 16, padding + 16,
          color, color,
          0
        )
      }
    }
  }

  override fun renderSlotContents(guiGraphics: GuiGraphics, itemstack: ItemStack, slot: Slot, countString: String?) {
    if (slot !is DynamicSlot<*>) return super.renderSlotContents(guiGraphics, itemstack, slot, countString)

    if (slot.isFake) {
      guiGraphics.renderFakeItem(itemstack, 0, 0, 0)
    } else {
      guiGraphics.renderItem(itemstack, 0, 0, 0)
    }

    guiGraphics.renderItemDecorations(this.font, itemstack, 0, 0, countString)
  }

  override fun isHovering(slot: Slot, mouseX: Double, mouseY: Double): Boolean {
    if (slot !is DynamicSlot<*>) return super.isHovering(slot, mouseX, mouseY)
    val padding = (slot.size - 16) / 2
    return this.isHovering((slot.actualX + padding).roundToInt(), (slot.actualY + padding).roundToInt(), 16, 16, mouseX, mouseY)
  }

  // layout helper
  fun TextureBox(texture: Texture, children: T7LayoutElement.() -> Unit) =
    Row({
      width = fixed(texture.width)
      height = fixed(texture.height)
    }) {
      addRenderableOnly(texture(texture))
      children()
    }
}