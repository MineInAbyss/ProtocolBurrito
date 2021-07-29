plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.17-R0.1-SNAPSHOT")
    implementation("com.comphenix.protocol:ProtocolLib:4.5.0")
    implementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.0")
    implementation("com.squareup:kotlinpoet:1.8.0")
    implementation(project(":protocolburrito-api"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.21-1.0.0-beta05")
    compileOnly("com.google.auto.service:auto-service:1.0")
    kapt("com.google.auto.service:auto-service:1.0")
}

//sourceSets.main {
//    java.srcDirs("src/main/kotlin")
//}


