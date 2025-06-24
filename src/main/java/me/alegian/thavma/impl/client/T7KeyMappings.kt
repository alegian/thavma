package me.alegian.thavma.impl.client

import me.alegian.thavma.impl.Thavma
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

object T7KeyMappings {
  val FOCI = KeyMapping(name("foci"), GLFW.GLFW_KEY_R, "thavma")
}

private fun name(id: String) = "key."+ Thavma.MODID + ".$id"