plugins {
    alias(idofrontLibs.plugins.mia.kotlin.jvm)
    id(idofrontLibs.plugins.mia.papermc.get().pluginId)
    alias(idofrontLibs.plugins.mia.nms)
    id(idofrontLibs.plugins.mia.copyjar.get().pluginId)
}

dependencies {
    compileOnly(idofrontLibs.bundles.idofront.core)
    implementation(project(":"))
}
