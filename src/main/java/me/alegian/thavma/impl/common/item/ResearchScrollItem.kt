package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag

class ResearchScrollItem : Item(Properties().stacksTo(1)) {
  override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
    val state = stack.get(T7DataComponents.RESEARCH_STATE) ?: return
    tooltipComponents.add(
      Component.translatable(
        ResearchEntry.translationId(state.researchEntry)
      ).withStyle(Rarity.UNCOMMON.styleModifier)
    )
  }

  override fun getName(stack: ItemStack): Component {
    val state = stack.get(T7DataComponents.RESEARCH_STATE)
    if (state?.completed == true) return Component.translatable(completedTranslation())
    return super.getName(stack)
  }

  fun completedTranslation(): String {
    return getOrCreateDescriptionId() + ".completed"
  }
}