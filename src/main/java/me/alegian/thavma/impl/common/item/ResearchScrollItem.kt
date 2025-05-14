package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.research.ResearchEntry
import me.alegian.thavma.impl.init.registries.deferred.T7DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class ResearchScrollItem : Item(Properties().stacksTo(1)) {
  override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
    val state = stack.get(T7DataComponents.RESEARCH_STATE) ?: return
    tooltipComponents.add(Component.translatable(ResearchEntry.translationId(state.researchEntry)))
    if (state.completed)
      tooltipComponents.add(Component.translatable(completedTranslation()))
  }

  fun completedTranslation(): String {
    return getOrCreateDescriptionId() + ".completed"
  }
}