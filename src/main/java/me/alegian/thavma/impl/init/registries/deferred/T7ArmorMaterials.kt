package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.rl
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredRegister

object T7ArmorMaterials {
  val REGISTRAR = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Thavma.MODID)

  val GOGGLES = REGISTRAR.register(
    "goggles"
  ) { ->
    ArmorMaterial(
      mapOf(
        ArmorItem.Type.HELMET to 1,
      ),
      25,
      SoundEvents.ARMOR_EQUIP_CHAIN,
      { Ingredient.of() },
      listOf(),
      0f,
      0f
    )
  }

  val APPRENTICE = REGISTRAR.register(
    "apprentice"
  ) { ->
    ArmorMaterial(
      mapOf(
        ArmorItem.Type.BOOTS to 1,
        ArmorItem.Type.LEGGINGS to 1,
        ArmorItem.Type.CHESTPLATE to 1,
      ),
      25,
      SoundEvents.ARMOR_EQUIP_LEATHER,
      { Ingredient.of() },
      listOf(),
      0f,
      0f
    )
  }

  val THAVMITE = REGISTRAR.register(
    "thavmite"
  ) { ->
    ArmorMaterial(
      mapOf(
        ArmorItem.Type.BOOTS to 2,
        ArmorItem.Type.LEGGINGS to 5,
        ArmorItem.Type.CHESTPLATE to 6,
        ArmorItem.Type.HELMET to 3
      ),
      25,
      SoundEvents.ARMOR_EQUIP_IRON,
      { Ingredient.of(T7Items.THAVMITE_INGOT) },
      listOf(
        ArmorMaterial.Layer(rl("thavmite"))
      ),
      1.0f,
      0f
    )
  }

  val THAVMITE_VANGUARD = REGISTRAR.register(
    "thavmite_vanguard"
  ) { ->
    ArmorMaterial(
      mapOf(
        ArmorItem.Type.BOOTS to 3,
        ArmorItem.Type.LEGGINGS to 6,
        ArmorItem.Type.CHESTPLATE to 8,
        ArmorItem.Type.HELMET to 3
      ),
      25,
      SoundEvents.ARMOR_EQUIP_NETHERITE,
      { Ingredient.of(T7Items.THAVMITE_INGOT) },
      listOf(
        ArmorMaterial.Layer(rl("thavmite_vanguard"))
      ),
      3.0f,
      0.1f
    )
  }
}
