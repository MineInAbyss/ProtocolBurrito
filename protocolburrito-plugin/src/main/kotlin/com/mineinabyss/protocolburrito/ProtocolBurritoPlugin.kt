package com.mineinabyss.protocolburrito

import com.mineinabyss.idofront.platforms.Platforms
import org.bukkit.plugin.java.JavaPlugin

class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onLoad() {
        Platforms.load(this, "mineinabyss")
    }
    override fun onEnable() {
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
