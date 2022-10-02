val idofrontVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.copyjar")
    id("com.mineinabyss.conventions.autoversion")
//    id("de.nycode.spigot-dependency-loader") version "1.0.3"
}

dependencies {
    implementation(project(":"))
    implementation("com.mineinabyss:idofront:$idofrontVersion")
}

tasks {
    shadowJar {
        minimize {
            include(dependency("com.mineinabyss:idofront.*:.*"))
        }
    }

    assemble {
        dependsOn(project(":protocolburrito-generator").tasks.assemble)
    }
}
