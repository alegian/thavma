package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.data.capability.AspectContainer
import me.alegian.thavma.impl.common.data.capability.SimpleCurio
import me.alegian.thavma.impl.common.item.*
import me.alegian.thavma.impl.common.util.DoubleMap
import me.alegian.thavma.impl.common.wand.WandCoreMaterial
import me.alegian.thavma.impl.common.wand.WandHandleMaterial
import me.alegian.thavma.impl.init.registries.T7Capabilities
import me.alegian.thavma.impl.init.registries.T7Tiers
import me.alegian.thavma.impl.init.registries.deferred.T7ArmorMaterials.ARCANUM
import net.minecraft.core.Registry
import net.minecraft.world.item.*
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.DeferredSpawnEggItem
import net.neoforged.neoforge.registries.DeferredRegister
import top.theillusivec4.curios.api.CuriosCapability

object T7Items {
  val REGISTRAR = DeferredRegister.createItems(Thavma.MODID)

  val IRON_HANDLE = REGISTRAR.registerSimpleItem("iron_handle")
  val GOLD_HANDLE = REGISTRAR.registerSimpleItem("gold_handle")
  val ORICHALCUM_HANDLE = REGISTRAR.registerSimpleItem("orichalcum_handle")
  val ARCANUM_HANDLE = REGISTRAR.registerSimpleItem("arcanum_handle")

  val EYE_OF_WARDEN = REGISTRAR.registerSimpleItem("eye_of_warden", Item.Properties().rarity(Rarity.EPIC))
  val ROTTEN_BRAIN = REGISTRAR.registerSimpleItem("rotten_brain", Item.Properties())
  val SIGIL = REGISTRAR.registerSimpleItem("sigil")

  val GREATWOOD_CORE = REGISTRAR.registerSimpleItem("greatwood_core")
  val SILVERWOOD_CORE = REGISTRAR.registerSimpleItem("silverwood_core")

  val RUNE = REGISTRAR.registerSimpleItem("rune")
  val ARCANUM_INGOT = REGISTRAR.registerSimpleItem("arcanum_ingot")
  val ARCANUM_NUGGET = REGISTRAR.registerSimpleItem("arcanum_nugget")
  val ORICHALCUM_INGOT = REGISTRAR.registerSimpleItem("orichalcum_ingot")
  val ORICHALCUM_NUGGET = REGISTRAR.registerSimpleItem("orichalcum_nugget")
  val RESEARCH_SCROLL = REGISTRAR.register("research_scroll", ::ResearchScrollItem)
  val COMPLETED_RESEARCH = REGISTRAR.registerSimpleItem("completed_research", Item.Properties().stacksTo(1))

  val ARCANUM_SWORD = REGISTRAR.register("arcanum_sword") { ->
    SwordItem(
      T7Tiers.ARCANUM_TIER,
      Item.Properties().attributes(
        SwordItem.createAttributes(
          T7Tiers.ARCANUM_TIER,
          3, -2.4f
        )
      )
    )
  }
  val ARCANUM_SHOVEL = REGISTRAR.register("arcanum_shovel") { ->
    ShovelItem(
      T7Tiers.ARCANUM_TIER,
      Item.Properties().attributes(
        ShovelItem.createAttributes(
          T7Tiers.ARCANUM_TIER,
          1.5f, -3.0f
        )
      )
    )
  }
  val ARCANUM_PICKAXE = REGISTRAR.register("arcanum_pickaxe") { ->
    PickaxeItem(
      T7Tiers.ARCANUM_TIER,
      Item.Properties().attributes(
        PickaxeItem.createAttributes(
          T7Tiers.ARCANUM_TIER,
          1.0f, -2.8f
        )
      )
    )
  }
  val ARCANUM_HAMMER = REGISTRAR.register("arcanum_hammer") { ->
    HammerItem(
      T7Tiers.ARCANUM_TIER,
      Item.Properties().attributes(
        HammerItem.createAttributes(
          T7Tiers.ARCANUM_TIER,
          4.0f, -3.0f
        )
      )
    )
  }
  val ARCANUM_AXE = REGISTRAR.register("arcanum_axe") { ->
    AxeItem(
      T7Tiers.ARCANUM_TIER,
      Item.Properties().attributes(
        AxeItem.createAttributes(
          T7Tiers.ARCANUM_TIER,
          5.0f, -3.0f
        )
      )
    )
  }
  val ARCANUM_HOE = REGISTRAR.register("arcanum_hoe") { ->
    HoeItem(
      T7Tiers.ARCANUM_TIER,
      Item.Properties().attributes(
        HoeItem.createAttributes(
          T7Tiers.ARCANUM_TIER,
          -3.0f, 0.0f
        )
      )
    )
  }
  val ARCANUM_KATANA = REGISTRAR.register("arcanum_katana", ::KatanaItem)
  val ZEPHYR = REGISTRAR.register("zephyr", ::ZephyrItem)
  val OCULUS = REGISTRAR.registerItem("oculus", ::OculusItem)
  val THAUMONOMICON = REGISTRAR.registerItem("thaumonomicon", ::ThaumonomiconItem)

  val GOGGLES = REGISTRAR.register("goggles", ::GogglesItem)
  val GOGGLES_CURIO = REGISTRAR.registerSimpleItem("goggles_curio", Item.Properties().stacksTo(1))
  val RESEARCHER_CHESTPLATE = REGISTRAR.registerItem(
    "researcher_chestplate",
    { props -> ResearcherArmorItem(ArmorItem.Type.CHESTPLATE, props) },
    Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))
  )
  val RESEARCHER_LEGGINGS = REGISTRAR.registerItem(
    "researcher_leggings",
    { props -> ResearcherArmorItem(ArmorItem.Type.LEGGINGS, props) },
    Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(21))
  )
  val RESEARCHER_BOOTS = REGISTRAR.registerItem(
    "researcher_boots",
    { props -> ResearcherArmorItem(ArmorItem.Type.BOOTS, props) },
    Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(21))
  )
  val ARCANUM_HELMET = REGISTRAR.registerItem(
    "arcanum_helmet",
    { props -> ArcanumHelmetItem(props) },
    Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(21))
  )
  val DAWN_CHARM = REGISTRAR.registerSimpleItem("charm_of_the_dawn", Item.Properties().stacksTo(1))
  val ARCANUM_CHESTPLATE = REGISTRAR.registerItem(
    "arcanum_chestplate",
    { props -> ArmorItem(ARCANUM, ArmorItem.Type.CHESTPLATE, props) },
    Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))
  )
  val ARCANUM_LEGGINGS = REGISTRAR.registerItem(
    "arcanum_leggings",
    { props -> ArmorItem(ARCANUM, ArmorItem.Type.LEGGINGS, props) },
    Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(21))
  )
  val ARCANUM_BOOTS = REGISTRAR.registerItem(
    "arcanum_boots",
    { props -> ArmorItem(ARCANUM, ArmorItem.Type.BOOTS, props) },
    Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(21))
  )
  val CUSTOS_ARCANUM_HELMET = REGISTRAR.registerItem(
    "custos_arcanum_helmet",
    { props -> CustosArcanumArmorItem(ArmorItem.Type.HELMET, props) },
    Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(21))
  )
  val CUSTOS_ARCANUM_CHESTPLATE =
    REGISTRAR.registerItem(
      "custos_arcanum_chestplate",
      { props -> CustosArcanumArmorItem(ArmorItem.Type.CHESTPLATE, props) },
      Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(21))
    )
  val CUSTOS_ARCANUM_LEGGINGS =
    REGISTRAR.registerItem(
      "custos_arcanum_leggings",
      { props -> CustosArcanumArmorItem(ArmorItem.Type.LEGGINGS, props) },
      Item.Properties().fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(21))
    )
  val CUSTOS_ARCANUM_BOOTS = REGISTRAR.registerItem(
    "custos_arcanum_boots",
    { props -> CustosArcanumArmorItem(ArmorItem.Type.BOOTS, props) },
    Item.Properties().fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(21))
  )

  val TESTAS = linkedMapWithPrimalKeys { aspect ->
    REGISTRAR.registerItem(aspect.id.path + "_testa") { _ -> TestaItem(aspect) }
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

    event.registerItem(
      CuriosCapability.ITEM,
      { itemStack, _ -> SimpleCurio(itemStack) },
      GOGGLES, DAWN_CHARM
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
    Registry.register(registry, Thavma.rl(wandName), newWand)
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
