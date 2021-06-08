package com.mineinabyss.protocolburrito

import com.mineinabyss.protocolburrito.generation.RunGenerator
import io.github.slimjar.app.builder.ApplicationBuilder
import org.bukkit.plugin.java.JavaPlugin

@RunGenerator
class ProtocolBurritoPlugin : JavaPlugin() {
    override fun onEnable() {
        logger.info("Downloading dependencies.")
        ApplicationBuilder.appending("ProtocolBurrito").build()
        logger.info("ProtocolBurrito is ready to start wrapping packets!")
    }
}
