package me.alegian.thavma.impl.common.util

import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3

object T7StreamCodecs{
    val VEC3 = StreamCodec.composite(
      ByteBufCodecs.DOUBLE,
      {it.x},
      ByteBufCodecs.DOUBLE,
      {it.y},
      ByteBufCodecs.DOUBLE,
      {it.z},
      ::Vec3
    )
}