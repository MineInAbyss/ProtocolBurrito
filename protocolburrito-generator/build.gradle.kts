@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(idofrontLibs.plugins.mia.kotlin.jvm)
    alias(idofrontLibs.plugins.mia.nms)
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("io.papermc.paper:paper-server:userdev-${idofrontLibs.versions.minecraft.get()}")
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
