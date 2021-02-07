package com.mineinabyss.protocolburrito

import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

fun generateEntityIdMapper() {
    generateNameToIDWrapper("PacketEntityType", "entities.json")
    generateNameToIDWrapper("PacketBlocks", "blocks.json")
    generateNameToIDWrapper("PacketItems", "items.json")
    generateNameToIDWrapper("PacketBiomes", "biomes.json")
}

fun generateNameToIDWrapper(
    className: String,
    fileName: String,
    packageName: String = "com.mineinabyss.protocolburrito.enums",
) {
    println("Generating enum for $fileName")
    val entitiesFile = File("$SERVER_PATH/$fileName").readText()
    val parsed: List<Map<String, Any?>> = JsonPath.parse(entitiesFile)?.read("$")!!

    val file = FileSpec.builder(packageName, className)
        .addType(
            TypeSpec
                .enumBuilder(className)
                //TODO is it worth having an id property or are we 100% guaranteed the ordinal is going to be id anyways?
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("id", Int::class)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("id", Int::class)
                        .initializer("id")
                        .build()
                )
                .apply {
                    parsed.forEach { type ->
                        val id = type["id"] as Int
                        val name = (type["name"] as String).toUpperCase()

                        addEnumConstant(
                            name, TypeSpec.anonymousClassBuilder()
                                .addSuperclassConstructorParameter("$id")
                                .build()
                        )
                    }
                }
                .build()
        )
        .build()

    file.writeTo(File(AnnotationProcessor.generatedDir))
}
