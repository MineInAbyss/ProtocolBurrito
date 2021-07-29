rootProject.name = "ProtocolBurrito"

pluginManagement {
    val miaConventionsVersion: String by settings

    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://repo.mineinabyss.com/releases")
    }

    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.mineinabyss.conventions"))
                useVersion(miaConventionsVersion)
        }
    }
}

include("generator", "shared")
