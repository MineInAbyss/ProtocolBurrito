package com.mineinabyss.protocolburrito

import org.bukkit.plugin.java.JavaPlugin

@RunGenerator
class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
