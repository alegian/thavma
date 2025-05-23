package me.alegian.thavma.impl.common.item

import me.alegian.thavma.impl.common.aspect.Aspect
import net.minecraft.world.item.Item
import java.util.function.Supplier

class ShardItem(val aspect: Supplier<Aspect>) : Item(Properties())
