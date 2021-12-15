import Com_mineinabyss_conventions_platform_gradle.Deps

val idofrontVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.copyjar")
    kotlin("kapt")
    id("de.nycode.spigot-dependency-loader") version "1.0.3"
}

dependencies {
    // MineInAbyss platform
    spigot(Deps.kotlin.stdlib)

    implementation(project(":"))
    implementation("com.mineinabyss:idofront:$idofrontVersion")
}

tasks {
    shadowJar {
        archiveBaseName.set("ProtocolBurrito")
        archiveClassifier.set("")
        minimize {
            include(dependency("com.mineinabyss:idofront.*:.*"))
        }
    }
}
