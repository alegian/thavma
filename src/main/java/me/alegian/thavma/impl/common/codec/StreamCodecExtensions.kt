package me.alegian.thavma.impl.common.codec

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import kotlin.jvm.optionals.getOrNull

fun <B : ByteBuf, T> StreamCodec<B, T>.listOf() = apply(ByteBufCodecs.list())

fun <B : ByteBuf, T> StreamCodec<B, T>.optional() = apply(ByteBufCodecs::optional)

fun <B : ByteBuf, T> StreamCodec<B, T>.nullable() = optional().map({ it.getOrNull() }, { Optional.ofNullable(it) })
