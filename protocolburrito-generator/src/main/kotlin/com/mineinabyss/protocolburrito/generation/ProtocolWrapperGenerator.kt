package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.events.PacketContainer
import com.mineinabyss.protocolburrito.WrappedPacket
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import com.squareup.kotlinpoet.*
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

val SERVER_VERSION = "1.17"
val PROJECT_ROOT = File(AnnotationProcessor.generatedDir).parentFile.parentFile.parentFile.parentFile.parentFile
val SERVER_PATH = File(PROJECT_ROOT, "minecraft-data/data/pc/$SERVER_VERSION/")

fun generateProtocolWrappers() {
    val protocolFile = File(SERVER_PATH, "protocol.json").readText()
    val parsed: Map<String, List<Any?>> =
        JsonPath.parse(protocolFile)?.read("$.play.toClient.types")!!

    parsed.filter { it.key != "packet" }
//        .filter { it.key == "packet_entity_move_look" }
        .forEach { entry ->
            runCatching {
                generateWrapper(
                    entry.key,
                    entry.value.dropWhile { it == "container" }
                        .filterIsInstance<List<HashMap<String, *>>>()
                        .first()
                )
            }.onFailure {
                println("Skipping ${entry.key} due to error")
                it.printStackTrace()
            }
        }
}

fun generateWrapper(packetName: String, params: List<HashMap<String, *>>) {
    val className = packetName.split("_").joinToString("") { it.capitalize() }
//    println("${this["name"]} ${this["type"]}")

//    val typeClasses: List<KClass<*>> = params.toSet().mapNotNull {
//        types[it["type"]]
//    }

    val indices = mutableMapOf<String, AtomicInteger>()
    val props: List<PropertySpec> = params.mapNotNull {
        val type = it["type"] as String
        val name = it["name"] as String

        val index = indices.getOrPut(type) { AtomicInteger(0) }

        val typeInfo = TypeMap.types[type] ?: return@mapNotNull null
        PropertySpec.builder(name, typeInfo.kClass)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return handle.${typeInfo.structureModified.name}().read(${index.toInt()})").build()
            )
            .setter(
                FunSpec.setterBuilder()
                    .addParameter(ParameterSpec.builder("value", typeInfo.kClass).build())
                    .addCode("handle.${typeInfo.structureModified.name}().write(${index.toInt()}, value)").build()
            )
            .mutable(true)
            .build().also {
                index.getAndAdd(1)
            }
    }

    val file = FileSpec.builder("com.mineinabyss.protocolburrito.packets", className)
        .addType(
            TypeSpec
                .classBuilder(className)
                .superclass(WrappedPacket::class)
                .addSuperclassConstructorParameter("handle")
                .primaryConstructor(
                    FunSpec
                        .constructorBuilder()
                        .addParameter("handle", PacketContainer::class)
                        .build()
                )
                .addProperties(props)
                .build()
        ).build()

    file.writeTo(File(AnnotationProcessor.generatedDir))
}
