package me.alegian.thavma.impl.client.gui.tooltip

import me.alegian.thavma.impl.common.aspect.AspectMap
import me.alegian.thavma.impl.init.registries.deferred.Aspects.DATAGEN_PRIMALS
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

fun containedPrimalsComponent(contents: AspectMap): MutableComponent {
  return Component.empty().also {
    for (i in DATAGEN_PRIMALS.indices) {
      val a = DATAGEN_PRIMALS[i].get()
      val newPart = Component.literal(contents[a].toString()).withColor(a.color)
      it.append(newPart)
      if (i != DATAGEN_PRIMALS.size - 1) it.append(Component.literal(" | "))
    }
  }
}

fun containedAspectsComponents(contents: AspectMap): List<MutableComponent> {
  return contents.map {
    Component.translatable(it.aspect.translationId).append(
      Component.literal(" × " + contents[it.aspect].toString())
    )
      .withColor(it.aspect.color)
  }
}
