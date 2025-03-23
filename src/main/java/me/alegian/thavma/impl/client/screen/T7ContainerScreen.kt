package me.alegian.thavma.impl.client.screen

import me.alegian.thavma.impl.client.texture.Texture
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

private val INVENTORY_BG = Texture("gui/container/inventory", 174, 97, 256, 256)
private val SLOT_TEXTURE = Texture("gui/container/slot", 18, 18)
private const val GAP = 4
private const val INVENTORY_PADDING = 6
private const val HOTBAR_GAP = 4

/**
 * The background texture's size is used to determine the size of the container
 */
abstract class T7ContainerScreen<T : Menu>(menu: T, pPlayerInventory: Inventory, pTitle: Component, private val bgTexture: Texture) : AbstractContainerScreen<T>(menu, pPlayerInventory, pTitle) {
  abstract fun layout()

  override fun init() {
    super.init()
    val lineHeight = font.lineHeight
    topPos = 0
    leftPos = 0

    Box(
      sizing=Sizing(fixed(width.toFloat()), fixed(height.toFloat()))
      //TODO: center
    ) {
      Box(sizing = Sizing(fixed(40f))){
        addRenderableOnly(debugRect(0xFFFF0000.toInt()))
      }
    }

//    Root(width, height) {
//      Box(
//        Modifier().center()
//      ) {
//        Column(
//          Modifier().size(bgTexture).extendVertically(INVENTORY_BG).extendVertically(GAP).extendVertically(lineHeight).centerX()
//        ) {
//          Box(Modifier().height(lineHeight)) {
//            addRenderableOnly(text(this@T7ContainerScreen.title, 0x83FF9B))
//          }
//          Box(Modifier().size(bgTexture)) {
//            addRenderableOnly(texture(bgTexture))
//            layout()
//          }
//          Box(Modifier().height(GAP))
//          Box(Modifier().size(INVENTORY_BG)) {
//            addRenderableOnly(texture(INVENTORY_BG))
//            PaddingX(INVENTORY_PADDING) {
//              Column {
//                Box(
//                  Modifier().height(INVENTORY_PADDING).extendVertically(lineHeight).centerY()
//                ) {
//                  Box(Modifier().height(lineHeight)) {
//                    addRenderableOnly(text(this@T7ContainerScreen.playerInventoryTitle, 0x404040))
//                  }
//                }
//                Column(Modifier().color(0xFF00FFFF.toInt())) {
//                  val inventorySlots = menu.playerInventory.range.slots
//                  Box(Modifier().height(SLOT_TEXTURE.height * 3)) {
//                    addRenderableOnly(slotGrid(3, 9, inventorySlots) { _, _ -> SLOT_TEXTURE })
//                  }
//                  Box(Modifier().height(HOTBAR_GAP))
//                  Box {
//                    addRenderableOnly(slotGrid(1, 9, inventorySlots.takeLast(9)) { _, _ -> SLOT_TEXTURE })
//                  }
//                }
//              }
//            }
//          }
//        }
//      }
//    }
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
    val i = slot.actualX + padding
    val j = slot.actualY + padding
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

    guiGraphics.pose().pushPose()
    guiGraphics.pose().translate(0.0f, 0.0f, 100.0f)
    if (itemStack.isEmpty && slot.isActive) {
      val pair = slot.noItemIcon
      if (pair != null) {
        val sprite = Minecraft.getInstance().getTextureAtlas(pair.first).apply(pair.second)
        guiGraphics.blit(i, j, 0, 16, 16, sprite)
        drawItem = true
      }
    }

    if (!drawItem) {
      if (quickReplace) {
        guiGraphics.fill(i, j, i + 16, j + 16, -2130706433)
      }

      renderSlotContents(guiGraphics, itemStack, slot, count)
    }

    guiGraphics.pose().popPose()
  }

  override fun renderSlotHighlight(guiGraphics: GuiGraphics, slot: Slot, mouseX: Int, mouseY: Int, partialTick: Float) {
    if (slot !is DynamicSlot<*>) return super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick)

    if (slot.isHighlightable) {
      val color = getSlotColor(slot.index)
      val padding = (slot.size - 16) / 2
      guiGraphics.fillGradient(
        RenderType.guiOverlay(),
        slot.actualX + padding, slot.actualY + padding,
        slot.actualX + padding + 16, slot.actualY + padding + 16,
        color, color,
        0
      )
    }
  }

  override fun renderSlotContents(guiGraphics: GuiGraphics, itemstack: ItemStack, slot: Slot, countString: String?) {
    if (slot !is DynamicSlot<*>) return super.renderSlotContents(guiGraphics, itemstack, slot, countString)

    val padding = (slot.size - 16) / 2
    val i = slot.actualX + padding
    val j = slot.actualY + padding
    val j1 = i + j * this.imageWidth
    if (slot.isFake) {
      guiGraphics.renderFakeItem(itemstack, i, j, j1)
    } else {
      guiGraphics.renderItem(itemstack, i, j, j1)
    }

    guiGraphics.renderItemDecorations(this.font, itemstack, i, j, countString)
  }

  override fun isHovering(slot: Slot, mouseX: Double, mouseY: Double): Boolean {
    if (slot !is DynamicSlot<*>) return super.isHovering(slot, mouseX, mouseY)
    val padding = (slot.size - 16) / 2
    return this.isHovering(slot.actualX + padding, slot.actualY + padding, 16, 16, mouseX, mouseY)
  }
}