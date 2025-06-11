package me.alegian.thavma.impl.common.data

import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.common.MutableDataComponentHolder
import java.util.function.Supplier

fun <T> MutableDataComponentHolder.update(type: Supplier<DataComponentType<T>>, updater: (T) -> T) {
  val currValue = get(type) ?: return
  set(type, updater(currValue))
}