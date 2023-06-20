import io.papermc.paperweight.util.registering

plugins {
    kotlin("jvm")
    id("com.mineinabyss.conventions.nms")
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("io.papermc.paper:paper-server:userdev-${libs.versions.minecraft.get()}")
    implementation(burritoLibs.minecraft.plugin.protocollib)
    implementation(burritoLibs.kotlinpoet)
    implementation(burritoLibs.reflections)
    implementation(project(":protocolburrito-api"))
}

configurations {
    remove(findByName("reobf"))
}

tasks {
    reobfJar {
        onlyIf { false }
    }
}
