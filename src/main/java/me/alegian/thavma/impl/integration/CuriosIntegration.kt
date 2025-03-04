package me.alegian.thavma.impl.integration

import me.alegian.thavma.impl.init.data.providers.T7ItemTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.neoforged.fml.ModList
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.data.event.GatherDataEvent

open class CuriosIntegration {
  companion object {
    private var INSTANCE = CuriosIntegration()

    fun init() {
      if (INSTANCE is UnsafeCuriosHooks) return
      if (!ModList.get().isLoaded("curios")) return

      INSTANCE = UnsafeCuriosHooks()
    }

    fun get(): CuriosIntegration {
      return INSTANCE
    }
  }

  open fun registerCapabilities(event: RegisterCapabilitiesEvent) {}

  open fun gatherData(event: GatherDataEvent) {}

  open fun addTags(provider: T7ItemTagProvider, lookupProvider: HolderLookup.Provider) {}

  open fun isWearingCurio(entity: LivingEntity, item: Item): Boolean {
    return false
  }
}