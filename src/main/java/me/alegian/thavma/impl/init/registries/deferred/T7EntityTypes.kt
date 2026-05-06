package me.alegian.thavma.impl.init.registries.deferred

import me.alegian.thavma.impl.Thavma
import me.alegian.thavma.impl.common.entity.AngryZombieEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.item.ItemEntity
import net.neoforged.neoforge.registries.DeferredRegister

object T7EntityTypes {
    val REGISTRAR = DeferredRegister.create(Registries.ENTITY_TYPE, Thavma.MODID)

    val FANCY_ITEM = REGISTRAR.register("fancy_item") { ->
        EntityType.Builder.of({ entityType, level -> ItemEntity(entityType, level) }, MobCategory.MISC)
            .sized(0.25f, 0.25f)
            .eyeHeight(0.2125f)
            .clientTrackingRange(6)
            .updateInterval(20)
            .build("fancy_item")
    }

    val ANGRY_ZOMBIE = REGISTRAR.register("angry_zombie") { ->
        EntityType.Builder.of({ entityType, level -> AngryZombieEntity(entityType, level) }, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .eyeHeight(1.74F)
            .passengerAttachments(2.0125F)
            .ridingOffset(-0.7F)
            .clientTrackingRange(8)
            .build("angry_zombie")
    }
}
