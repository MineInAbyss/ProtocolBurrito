package com.mineinabyss.protocolburrito

import com.mineinabyss.idofront.slimjar.LibraryLoaderInjector
import org.bukkit.plugin.java.JavaPlugin

class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onEnable() {
        LibraryLoaderInjector.inject(this)
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
