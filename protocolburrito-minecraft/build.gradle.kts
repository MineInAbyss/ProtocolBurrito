plugins {
    id("com.mineinabyss.conventions.kotlin")
    id("com.mineinabyss.conventions.papermc")
    kotlin("kapt")
}

dependencies {
    slim(kotlin("stdlib-jdk8"))
    implementation(project(":"))
    implementation("com.mineinabyss:idofront:1.17.1-0.6.23")
}
