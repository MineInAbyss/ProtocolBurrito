@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    id(libs.plugins.mia.papermc.get().pluginId)
    alias(libs.plugins.mia.publication)
    alias(libs.plugins.mia.autoversion)
}

dependencies {
    compileOnly(libs.kotlin.reflect)
}
