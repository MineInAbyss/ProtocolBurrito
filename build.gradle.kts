import io.papermc.paperweight.util.registering

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    alias(libs.plugins.mia.nms)
    alias(libs.plugins.mia.publication)
    alias(libs.plugins.mia.autoversion)
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

sourceSets["main"].java.srcDir(file("$buildDir/generated/burrito/main"))

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    build {
        dependsOn(project(":protocolburrito-plugin").tasks.build)
    }

    val generateBurrito by registering<JavaExec> {
        mainClass.set("com.mineinabyss.protocolburrito.generation.MainKt")
        classpath = project("protocolburrito-generator").sourceSets["main"].runtimeClasspath
        outputs.dir("$buildDir/generated/burrito/main")
    }
    sourcesJar {
        dependsOn(generateBurrito)
    }
    compileKotlin {
        dependsOn(generateBurrito)
    }
}
