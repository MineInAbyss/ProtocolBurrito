import io.papermc.paperweight.util.registering

val serverVersion: String by project

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
    implementation("io.papermc.paper:paper-server:userdev-$serverVersion")
    implementation(mylibs.minecraft.plugin.protocollib)
    implementation(mylibs.kotlinpoet)
    implementation(mylibs.reflections)
    implementation(project(":protocolburrito-api"))
    implementation(kotlin("reflect"))
}

configurations {
    remove(findByName("reobf"))
}

tasks {
    val generateBurrito by registering<JavaExec>() {
        main = "com.mineinabyss.protocolburrito.generation.MainKt"
        classpath = files(configurations.runtimeClasspath, jar)
    }
    reobfJar {
        onlyIf { false }
    }
    build {
        dependsOn(generateBurrito)
    }
}
