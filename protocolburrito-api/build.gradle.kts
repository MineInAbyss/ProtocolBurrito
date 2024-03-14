@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(idofrontLibs.plugins.mia.kotlin.jvm)
    id(idofrontLibs.plugins.mia.papermc.get().pluginId)
    alias(idofrontLibs.plugins.mia.publication)
    alias(idofrontLibs.plugins.mia.autoversion)
}

dependencies {
    compileOnly(idofrontLibs.kotlin.reflect)
}
