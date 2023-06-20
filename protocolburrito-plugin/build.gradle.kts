val idofrontVersion: String by project

plugins {
    id("com.mineinabyss.conventions.kotlin.jvm")
    id("com.mineinabyss.conventions.papermc")
    id("com.mineinabyss.conventions.nms")
    id("com.mineinabyss.conventions.copyjar")
}

dependencies {
    implementation(project(":"))
    implementation(libs.bundles.idofront.core)
}
