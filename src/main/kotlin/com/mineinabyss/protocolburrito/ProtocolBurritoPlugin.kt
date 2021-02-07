package com.mineinabyss.protocolburrito

import com.mineinabyss.protocolburrito.generation.RunGenerator
import org.bukkit.plugin.java.JavaPlugin

@RunGenerator
class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
