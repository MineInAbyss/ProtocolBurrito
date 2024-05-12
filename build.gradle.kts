import io.papermc.paperweight.util.registering

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(idofrontLibs.plugins.mia.kotlin.jvm)
    alias(idofrontLibs.plugins.mia.nms)
    alias(idofrontLibs.plugins.mia.publication)
    alias(idofrontLibs.plugins.mia.autoversion)
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
        maven("https://repo.mineinabyss.com/snapshots/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.dmulloy2.net/nexus/repository/public/")//ProtocolLib
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    val libs = rootProject.idofrontLibs

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly(libs.minecraft.plugin.protocollib)
    }
}

dependencies {
    api(project(":protocolburrito-api"))
    api(idofrontLibs.minecraft.plugin.protocollib)
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
