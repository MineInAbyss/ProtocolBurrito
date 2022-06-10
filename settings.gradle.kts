rootProject.name = "protocolburrito"

pluginManagement {
    val idofrontVersion: String by settings

    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        google()
        maven("https://repo.mineinabyss.com/releases")
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.mineinabyss.conventions"))
                useVersion(idofrontVersion)
        }
    }
}

dependencyResolutionManagement {
    val idofrontVersion: String by settings

    repositories {
        mavenLocal()
        maven("https://repo.mineinabyss.com/releases")
    }

    versionCatalogs {
        create("libs") {
            from("com.mineinabyss:catalog:$idofrontVersion")
        }
    }
}

include(
    "protocolburrito-generator",
    "protocolburrito-api",
    "protocolburrito-plugin"
)
