package com.mineinabyss.protocolburrito

import com.mineinabyss.idofront.slimjar.LibraryLoaderInjector
import com.mineinabyss.protocolburrito.generation.RunGenerator
import org.bukkit.plugin.java.JavaPlugin

@RunGenerator
class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onEnable() {
        LibraryLoaderInjector.inject(this)
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
