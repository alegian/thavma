package me.alegian.thavma.impl.common.codec

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import kotlin.jvm.optionals.getOrNull

fun <T> StreamCodec<ByteBuf, T>.listOf() = apply(ByteBufCodecs.list())

fun <T> StreamCodec<ByteBuf, T>.optional() = apply(ByteBufCodecs::optional)

fun <T> StreamCodec<ByteBuf, T>.nullable() = optional().map({ it.getOrNull() }, { Optional.ofNullable(it) })
