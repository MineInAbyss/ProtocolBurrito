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
    val generateBurrito by registering<JavaExec>() {
        mainClass.set("com.mineinabyss.protocolburrito.generation.MainKt")
        classpath(files(configurations.runtimeClasspath, jar))
    }
    reobfJar {
        onlyIf { false }
    }
    build {
        dependsOn(generateBurrito)
    }
}
