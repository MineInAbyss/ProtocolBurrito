plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.mineinabyss.conventions.publication")
    id("de.undercouch.download") version "4.1.2"
    id("com.mineinabyss.conventions.nms")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

repositories {
    mavenCentral()
    google()
}

allprojects {
    apply(plugin = "java")

    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")//ProtocolLib
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0") {
            // this dep wasn"t being resolved.
            exclude(group = "com.comphenix.executors")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    implementation(project(":protocolburrito-api"))
    compileOnly(project(":protocolburrito-generator"))
    ksp(project(":protocolburrito-generator"))
}


sourceSets["main"].java.srcDir(file("$rootDir/protocolburrito-generator/build/generated/burrito/main"))

tasks {
//    val downloadMappings by register<de.undercouch.gradle.tasks.download.Download>("Download mappings") {
//        src("https://launcher.mojang.com/v1/objects/f6cae1c5c1255f68ba4834b16a0da6a09621fe13/server.txt")
//        dest(file("mappings/server.txt"))
//    }
    shadowJar {
        archiveClassifier.set("")
    }

    sourcesJar {
        from(sourceSets.main.get().allSource)
    }

//    publish {
//        dependsOn(downloadMappings)
//    }

    build {
        dependsOn(/*downloadMappings,*/ project(":protocolburrito-plugin").tasks.build)
    }
}
