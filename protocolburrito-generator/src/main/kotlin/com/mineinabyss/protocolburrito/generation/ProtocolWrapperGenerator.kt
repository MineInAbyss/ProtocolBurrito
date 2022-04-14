package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.events.PacketContainer
import com.mineinabyss.protocolburrito.FieldHelpers
import com.mineinabyss.protocolburrito.WrappedPacket
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import net.minecraft.network.protocol.Packet
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.WildcardType
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KVisibility
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

//val SERVER_VERSION = "1.17"
//val PROJECT_ROOT = File("")//File(AnnotationProcessor.generatedDir)//.parentFile.parentFile.parentFile.parentFile.parentFile
//val SERVER_PATH = File(PROJECT_ROOT, "minecraft-data/data/pc/$SERVER_VERSION/")
//val MAPPINGS = File(PROJECT_ROOT, "mappings/server.txt")


@OptIn(DelicateKotlinPoetApi::class, ExperimentalStdlibApi::class)
fun generateProtocolExtensions() {
    val classLoader = AnnotationProcessor::class.java.classLoader
    val reflections = Reflections("net.minecraft.network.protocol.game", SubTypesScanner(false))
    reflections.getSubTypesOf(Packet::class.java).forEach { packetClass ->
        val indices = mutableMapOf<Class<*>, AtomicInteger>()
        val props = packetClass.declaredFields.mapNotNull { field ->
            if(field.type.kotlin.visibility != KVisibility.PUBLIC) return@mapNotNull null
            val index = indices.getOrPut(field.type) { AtomicInteger(0) }
            PropertySpec.builder(field.name, field.type.let {
                if(field.type.typeParameters.isNotEmpty()) {
                    if(it != EntityType::class.java) return@mapNotNull null
                    it.asClassName().parameterizedBy(
                        WildcardTypeName.producerOf(Entity::class.java)
                    )
                }
                else it.asTypeName()
            })
                .receiver(packetClass)
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("return %T.getField(this, %T::class.java, ${index.toInt()})", FieldHelpers::class, field.type)
                        .build()
                )
                .setter(
                    FunSpec.setterBuilder()
                        .addParameter(ParameterSpec.builder("value", field.type).build())
                        .addCode("%T.setField(this, %T::class.java, ${index.toInt()}, value)", FieldHelpers::class, field.type)
                        .build()
                )
                .mutable(true)
                .build().also {
                    index.getAndAdd(1)
                }
        }

        val file = FileSpec.builder("com.mineinabyss.protocolburrito.packets", packetClass.simpleName + "Extensions")
            .apply {
                for(prop in props) {
                    addProperty(prop)
                }
            }
            .build()

        file.writeTo(File(AnnotationProcessor.generatedDir))
    }
}
/*
data class Prop(
    val typeInfo: TypeMap.TypeInfo<*>,
    val name: String,
)


fun generateProtocolWrappers() {
    var className: String? = null
    var props: MutableList<Property>? = null
    val mappings = MAPPINGS.forEachLine { line ->
        when {
            line.startsWith("net.minecraft.network.protocol.game") -> {
                if (className != null && props?.isNotEmpty() == true) {
                    runCatching {
                        generateWrapper(className!!, props!!)
                    }.onFailure {
                        println("Skipping $className due to error")
                        it.printStackTrace()
                    }
                }
                className =
                    line.removePrefix("net.minecraft.network.protocol.game.").substringBefore(" ").substringBefore("$")
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
*/
