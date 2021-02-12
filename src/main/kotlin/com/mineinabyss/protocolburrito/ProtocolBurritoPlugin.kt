package com.mineinabyss.protocolburrito

import com.mineinabyss.protocolburrito.generation.RunGenerator
import me.bristermitten.pdm.PluginDependencyManager
import org.bukkit.plugin.java.JavaPlugin

@RunGenerator
class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("Downloading dependencies.")
        PluginDependencyManager.of(this).loadAllDependencies().join()
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
