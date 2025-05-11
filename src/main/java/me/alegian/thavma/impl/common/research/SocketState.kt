package me.alegian.thavma.impl.common.research

import me.alegian.thavma.impl.common.aspect.Aspect

data class SocketState(val aspect: Aspect?, val broken: Boolean) {
  fun withAspect(a: Aspect?) = SocketState(a, broken)
  fun withBroken(b: Boolean) = SocketState(aspect, b)
}