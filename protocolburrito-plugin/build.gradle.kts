val idofrontVersion: String by project

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.mia.kotlin.jvm)
    id(libs.plugins.mia.papermc.get().pluginId)
    alias(libs.plugins.mia.nms)
    id(libs.plugins.mia.copyjar.get().pluginId)
}

dependencies {
    implementation(project(":"))
    implementation(libs.bundles.idofront.core)
}
