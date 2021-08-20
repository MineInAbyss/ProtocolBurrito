package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.events.PacketContainer
import com.mineinabyss.protocolburrito.WrappedPacket
import com.squareup.kotlinpoet.*
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

val SERVER_VERSION = "1.17"
val PROJECT_ROOT = File(AnnotationProcessor.generatedDir).parentFile.parentFile.parentFile.parentFile.parentFile
val SERVER_PATH = File(PROJECT_ROOT, "minecraft-data/data/pc/$SERVER_VERSION/")
val MAPPINGS = File(PROJECT_ROOT, "mappings/server.txt")

fun generateProtocolWrappers() {
    var className: String? = null
    var props: MutableList<Property>? = null
    val mappings = MAPPINGS.forEachLine { line -> 
        when {
            line.startsWith("net.minecraft.network.protocol.game") -> {
                if(className != null && props?.isNotEmpty() == true) {
                    runCatching {
                        generateWrapper(className!!, props!!)
                    }.onFailure {
                        println("Skipping $className due to error")
                        it.printStackTrace()
                    }
                }
                className = line.removePrefix("net.minecraft.network.protocol.game.").substringBefore(" ").substringBefore("$")
                props = mutableListOf<Property>()
            }
            line.startsWith("    ") && props != null -> {
                val (type, name, _, fieldName) = line.drop(4).split(" ")
                Property.of(type, name)?.let { props!! += it }
            }
        }
    }

}

data class Property(
    val typeInfo: TypeMap.TypeInfo<*>,
    val name: String,
) {
    companion object {
        fun of(type: String, name: String): Property? {
            return Property(TypeMap.types[type] ?: return null, name)
        }
    }
}

fun generateWrapper(packetName: String, params: List<Property>) {
    val className = packetName.split("_").joinToString("") { it.capitalize() }
//    println("${this["name"]} ${this["type"]}")

//    val typeClasses: List<KClass<*>> = params.toSet().mapNotNull {
//        types[it["type"]]
//    }

    val indices = mutableMapOf<TypeMap.TypeInfo<*>, AtomicInteger>()
    val props: List<PropertySpec> = params.mapNotNull { (typeInfo, name) ->
        val index = indices.getOrPut(typeInfo) { AtomicInteger(0) }

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
