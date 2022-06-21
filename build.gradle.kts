val serverVersion: String by project

plugins {
    kotlin("jvm")
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.publication")
    id("com.github.johnrengelman.shadow") version "7.0.0"
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
        mavenLocal()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")//ProtocolLib
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0-SNAPSHOT") {
            // this dep wasn"t being resolved.
            exclude(group = "com.comphenix.executors")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:$serverVersion")
    api(project(":protocolburrito-api"))
}

sourceSets["main"].java.srcDir(file("$rootDir/protocolburrito-generator/build/generated/burrito/main"))

tasks {
    assemble {
        dependsOn(reobfJar)
        dependsOn(project(":protocolburrito-plugin").tasks.build)
    }

    shadowJar {
        archiveClassifier.set("")
    }

    sourcesJar {
        from(sourceSets.main.get().allSource)
    }
}
