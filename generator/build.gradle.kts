plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    kotlin("kapt")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.0")
    implementation("com.squareup:kotlinpoet:1.8.0")
    implementation(project(":shared"))

    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.21-1.0.0-beta05")
    compileOnly("com.google.auto.service:auto-service:1.0")
    kapt("com.google.auto.service:auto-service:1.0")
}

//sourceSets.main {
//    java.srcDirs("src/main/kotlin")
//}


