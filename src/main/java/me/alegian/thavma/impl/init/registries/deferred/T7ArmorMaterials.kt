package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.rl
import net.minecraft.Util
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

object T7ArmorMaterials {
    val REGISTRAR = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Thavma.MODID)

    val GOGGLES = REGISTRAR.register(
        "goggles"
    ) { ->
        ArmorMaterial(
            Util.make(
                EnumMap(ArmorItem.Type::class.java)
            ) { map ->
                map[ArmorItem.Type.HELMET] = 1
            },
            25,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            { Ingredient.of() },
            listOf(),
            0f,
            0f
        )
    }

    val RESEARCHER = REGISTRAR.register(
        "researcher"
    ) { ->
        ArmorMaterial(
            Util.make(
                EnumMap(ArmorItem.Type::class.java)
            ) { map ->
                map[ArmorItem.Type.CHESTPLATE] = 1
                map[ArmorItem.Type.LEGGINGS] = 1
                map[ArmorItem.Type.BOOTS] = 1
            },
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            { Ingredient.of() },
            listOf(),
            0f,
            0f
        )
    }

    val ARCANUM = REGISTRAR.register(
        "arcanum"
    ) { ->
        ArmorMaterial(
            Util.make(
                EnumMap(ArmorItem.Type::class.java)
            ) { map ->
                map[ArmorItem.Type.BOOTS] = 2
                map[ArmorItem.Type.LEGGINGS] = 5
                map[ArmorItem.Type.CHESTPLATE] = 6
                map[ArmorItem.Type.HELMET] = 3
            },
            25,
            SoundEvents.ARMOR_EQUIP_IRON,
            { Ingredient.of(T7Items.THAVMITE_INGOT) },
            listOf(
                ArmorMaterial.Layer(rl("arcanum"))
            ),
            1.0f,
            0f
        )
    }

    val CUSTOS_ARCANUM = REGISTRAR.register(
        "custos_arcanum"
    ) { ->
        ArmorMaterial(
            Util.make(
                EnumMap(ArmorItem.Type::class.java)
            ) { map ->
                map[ArmorItem.Type.BOOTS] = 3
                map[ArmorItem.Type.LEGGINGS] = 6
                map[ArmorItem.Type.CHESTPLATE] = 8
                map[ArmorItem.Type.HELMET] = 3
            },
            25,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            { Ingredient.of(T7Items.THAVMITE_INGOT) },
            listOf(
                ArmorMaterial.Layer(rl("arcanum"))
            ),
            3.0f,
            0.1f
        )
    }
}
