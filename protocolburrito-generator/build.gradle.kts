import io.papermc.paperweight.util.registering

val serverVersion: String by project

plugins {
    kotlin("jvm")
    id("io.papermc.paperweight.userdev")
}

repositories {
    mavenCentral()
    google()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperDevBundle(serverVersion)
    implementation(kotlin("reflect"))
    implementation("io.papermc.paper:paper-server:userdev-1.18.2-R0.1-SNAPSHOT")
    implementation("com.comphenix.protocol:ProtocolLib:4.7.0")
    implementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.0")
    implementation("com.squareup:kotlinpoet:1.10.1")
    implementation(project(":protocolburrito-api"))

//    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
//    compileOnly("com.google.auto.service:auto-service:1.0")
    implementation("org.reflections:reflections:0.9.12")
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
//sourceSets.main {
//    java.srcDirs("src/main/kotlin")
//}


