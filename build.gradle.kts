plugins {
    id("com.mineinabyss.conventions.kotlin.jvm")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.publication")
    id("com.mineinabyss.conventions.autoversion")
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

allprojects {
    apply(plugin = "java")

    version = rootProject.version

    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")//ProtocolLib
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    val libs = rootProject.libs

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly(libs.minecraft.plugin.protocollib) {
            // this dep wasn't being resolved.
            exclude(group = "com.comphenix.executors")
        }
    }
}

dependencies {
    api(project(":protocolburrito-api"))
}

sourceSets["main"].java.srcDir(file("$rootDir/protocolburrito-generator/build/generated/burrito/main"))

tasks {
    assemble {
        dependsOn(reobfJar)
        dependsOn(project(":protocolburrito-plugin").tasks.build)
    }
}
