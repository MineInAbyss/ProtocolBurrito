plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.mineinabyss.conventions.publication")
//    id("com.google.devtools.ksp") version "1.5.21-1.0.0-beta05"
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
        compileOnly("com.comphenix.protocol:ProtocolLib:4.5.0") {
            // this dep wasn"t being resolved.
            exclude(group = "com.comphenix.executors")
        }
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    implementation(project(":protocolburrito-api"))
    compileOnly(project(":protocolburrito-generator"))
    kapt(project(":protocolburrito-generator"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }

    sourcesJar {
        from(sourceSets.main.get().allSource)
        from("$buildDir/generated/source/kaptKotlin/main")
    }

    build {
        dependsOn(project(":protocolburrito-minecraft").tasks.build)
    }
}

//sourceSets {
//    main {
//        java {
//            srcDir("build/generated/ksp/main/kotlin/")
//        }
//    }
//}
//
//ksp {
//    arg("minecraftDataDir", file("minecraft-data/data/pc/1.17").absolutePath)
//}
