package com.mineinabyss.protocolburrito

import com.mineinabyss.idofront.platforms.IdofrontPlatforms
import org.bukkit.plugin.java.JavaPlugin

class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onLoad() {
        IdofrontPlatforms.load(this, "mineinabyss")
    }
    override fun onEnable() {
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
