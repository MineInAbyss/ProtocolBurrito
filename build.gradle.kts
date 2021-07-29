plugins {
//    kotlin("jvm")
//    kotlin("")
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.publication")
    kotlin("kapt")
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
    slim(kotlin("stdlib-jdk8"))
    implementation(project(":shared"))
    implementation("com.mineinabyss:idofront:1.17.1-0.6.23")

    compileOnly(project(":generator"))
    kapt(project(":generator"))
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
