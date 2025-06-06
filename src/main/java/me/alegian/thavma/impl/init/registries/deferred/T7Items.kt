package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.item.*
import me.alegian.thavma.impl.common.util.DoubleMap
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.init.registries.T7Capabilities
import me.alegian.thavma.impl.init.registries.T7Tiers
import me.alegian.thavma.impl.init.registries.deferred.T7ArmorMaterials.THAVMITE
import me.alegian.thavma.impl.rl
import net.minecraft.core.Registry
import net.minecraft.world.item.*
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.DeferredSpawnEggItem
import net.neoforged.neoforge.registries.DeferredRegister

object T7Items {
  val REGISTRAR = DeferredRegister.createItems(Thavma.MODID)

  val IRON_HANDLE = REGISTRAR.registerSimpleItem("iron_handle")
  val GOLD_HANDLE = REGISTRAR.registerSimpleItem("gold_handle")
  val ORICHALCUM_HANDLE = REGISTRAR.registerSimpleItem("orichalcum_handle")
  val THAVMITE_HANDLE = REGISTRAR.registerSimpleItem("thavmite_handle")

  val EYE_OF_WARDEN = REGISTRAR.registerSimpleItem("eye_of_warden", Item.Properties().rarity(Rarity.EPIC))
  val ROTTEN_BRAIN = REGISTRAR.registerSimpleItem("rotten_brain", Item.Properties())
  val SIGIL = REGISTRAR.registerSimpleItem("sigil")
  val FABRIC = REGISTRAR.registerSimpleItem("fabric")

  val GREATWOOD_CORE = REGISTRAR.registerSimpleItem("greatwood_core")
  val SILVERWOOD_CORE = REGISTRAR.registerSimpleItem("silverwood_core")

  val RUNE = REGISTRAR.registerSimpleItem("rune")
  val THAVMITE_INGOT = REGISTRAR.registerSimpleItem("thavmite_ingot")
  val THAVMITE_NUGGET = REGISTRAR.registerSimpleItem("thavmite_nugget")
  val ORICHALCUM_INGOT = REGISTRAR.registerSimpleItem("orichalcum_ingot")
  val ORICHALCUM_NUGGET = REGISTRAR.registerSimpleItem("orichalcum_nugget")
  val RESEARCH_SCROLL = REGISTRAR.register("research_scroll", ::ResearchScrollItem)

  val THAVMITE_SWORD = REGISTRAR.register("thavmite_sword") { ->
    SwordItem(
      T7Tiers.THAVMITE_TIER,
      Item.Properties().attributes(
        SwordItem.createAttributes(
          T7Tiers.THAVMITE_TIER,
          3, -2.4f
        )
      )
    )
  }
  val THAVMITE_SHOVEL = REGISTRAR.register("thavmite_shovel") { ->
    ShovelItem(
      T7Tiers.THAVMITE_TIER,
      Item.Properties().attributes(
        ShovelItem.createAttributes(
          T7Tiers.THAVMITE_TIER,
          1.5f, -3.0f
        )
      )
    )
  }
  val THAVMITE_PICKAXE = REGISTRAR.register("thavmite_pickaxe") { ->
    PickaxeItem(
      T7Tiers.THAVMITE_TIER,
      Item.Properties().attributes(
        PickaxeItem.createAttributes(
          T7Tiers.THAVMITE_TIER,
          1.0f, -2.8f
        )
      )
    )
  }
  val THAVMITE_HAMMER = REGISTRAR.register("thavmite_hammer") { ->
    HammerItem(
      T7Tiers.THAVMITE_TIER,
      Item.Properties().attributes(
        DiggerItem.createAttributes(
          T7Tiers.THAVMITE_TIER,
          4.0f, -3.0f
        )
      )
    )
  }
  val THAVMITE_AXE = REGISTRAR.register("thavmite_axe") { ->
    AxeItem(
      T7Tiers.THAVMITE_TIER,
      Item.Properties().attributes(
        AxeItem.createAttributes(
          T7Tiers.THAVMITE_TIER,
          5.0f, -3.0f
        )
      )
    )
  }
  val THAVMITE_HOE = REGISTRAR.register("thavmite_hoe") { ->
    HoeItem(
      T7Tiers.THAVMITE_TIER,
      Item.Properties().attributes(
        HoeItem.createAttributes(
          T7Tiers.THAVMITE_TIER,
          -3.0f, 0.0f
        )
      )
    )
  }

  val THAVMITE_KATANA = REGISTRAR.register("thavmite_katana", ::KatanaItem)
  val ZEPHYR = REGISTRAR.register("zephyr", ::ZephyrItem)

  val ARCANE_LENS = REGISTRAR.registerItem("arcane_lens", ::ArcaneLensItem)
  val BOOK = REGISTRAR.register("book", ::T7BookItem)

  val GOGGLES = REGISTRAR.register("goggles", ::GogglesItem)
  val GOGGLES_CURIO = REGISTRAR.registerSimpleItem("goggles_curio", Item.Properties().stacksTo(1))
  val DAWN_CHARM = REGISTRAR.registerSimpleItem("charm_of_the_dawn", Item.Properties().stacksTo(1))

  val APPRENTICE_CHESTPLATE = REGISTRAR.registerItem(
    "apprentice_chestplate",
    { props -> ApprenticeArmorItem(ArmorItem.Type.CHESTPLATE, props) },
    Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))
  )
  val APPRENTICE_LEGGINGS = REGISTRAR.registerItem(
    "apprentice_leggings",
    { props -> ApprenticeArmorItem(ArmorItem.Type.LEGGINGS, props) },
    Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(21))
  )
  val APPRENTICE_BOOTS = REGISTRAR.registerItem(
    "apprentice_boots",
    { props -> ApprenticeArmorItem(ArmorItem.Type.BOOTS, props) },
    Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(21))
  )

  val THAVMITE_HELMET = REGISTRAR.registerItem(
    "thavmite_helmet",
    { props -> ThavmiteHelmetItem(props) },
    Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(21))
  )
  val THAVMITE_CHESTPLATE = REGISTRAR.registerItem(
    "thavmite_chestplate",
    { props -> ArmorItem(THAVMITE, ArmorItem.Type.CHESTPLATE, props) },
    Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))
  )
  val THAVMITE_LEGGINGS = REGISTRAR.registerItem(
    "thavmite_leggings",
    { props -> ArmorItem(THAVMITE, ArmorItem.Type.LEGGINGS, props) },
    Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(21))
  )
  val THAVMITE_BOOTS = REGISTRAR.registerItem(
    "thavmite_boots",
    { props -> ArmorItem(THAVMITE, ArmorItem.Type.BOOTS, props) },
    Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(21))
  )

  val THAVMITE_VANGUARD_HELMET = REGISTRAR.registerItem(
    "thavmite_vanguard_helmet",
    { props -> ThavmiteVanguardArmorItem(ArmorItem.Type.HELMET, props) },
    Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(21))
  )
  val THAVMITE_VANGUARD_CHESTPLATE =
    REGISTRAR.registerItem(
      "thavmite_vanguard_chestplate",
      { props -> ThavmiteVanguardArmorItem(ArmorItem.Type.CHESTPLATE, props) },
      Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))
    )
  val THAVMITE_VANGUARD_LEGGINGS =
    REGISTRAR.registerItem(
      "thavmite_vanguard_leggings",
      { props -> ThavmiteVanguardArmorItem(ArmorItem.Type.LEGGINGS, props) },
      Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(21))
    )
  val THAVMITE_VANGUARD_BOOTS = REGISTRAR.registerItem(
    "thavmite_vanguard_boots",
    { props -> ThavmiteVanguardArmorItem(ArmorItem.Type.BOOTS, props) },
    Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(21))
  )

  val SHARDS = linkedMapWithPrimalKeys { aspect ->
    REGISTRAR.registerItem(aspect.id.path + "_shard") { _ -> ShardItem(aspect) }
  }

  val ANGRY_ZOMBIE_SPAWN_EGG = REGISTRAR.registerItem("angry_zombie_spawn_egg") { p -> DeferredSpawnEggItem(T7EntityTypes.ANGRY_ZOMBIE, 0x00AFAF, 0x9e2323, p) }

  // (handleName, coreName)->wand. populated on Item Registry bake
  val WANDS = DoubleMap<String, String, WandItem>()

  fun registerCapabilities(event: RegisterCapabilitiesEvent) {
    for (wand in WANDS.values()) event.registerItem(
      T7Capabilities.AspectContainer.ITEM,
      { itemStack, _ -> AspectContainer(itemStack, wand.capacity()) },
      wand
    )
  }

  /**
   * Registers a wand with the given handle and core materials
   */
  fun registerWand(registry: Registry<Item>, handleMaterial: WandHandleMaterial, coreMaterial: WandCoreMaterial) {
    val handleName = handleMaterial.registeredName
    val coreName = coreMaterial.registeredName
    val wandName = WandItem.name(handleMaterial, coreMaterial)

    val newWand = WandItem(Item.Properties(), handleMaterial, coreMaterial)
    Registry.register(registry, rl(wandName), newWand)
    WANDS.put(handleName, coreName, newWand)
  }

  /**
   * Helper that gets a wand from the DoubleMap of registered wands.
   * WARNING: cannot get wands from addons, these have to be accessed manually.
   */
  fun wandOrThrow(handleMaterial: WandHandleMaterial, coreMaterial: WandCoreMaterial): WandItem {
    val handleName = handleMaterial.registeredName
    val coreName = coreMaterial.registeredName
    val wand = WANDS[handleName, coreName]

    requireNotNull(wand) {
      "Thavma Exception: Trying to Access Unregistered Wand Combination" + WandItem.name(
        handleMaterial,
        coreMaterial
      )
    }

    return wand
  }

  fun isWandRegistered(handleMaterial: WandHandleMaterial, coreMaterial: WandCoreMaterial): Boolean {
    val handleName = handleMaterial.registeredName
    val coreName = coreMaterial.registeredName
    return WANDS[handleName, coreName] != null
  }
}
