package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.injector.packet.PacketRegistry
import com.mineinabyss.protocolburrito.FieldHelpers
import com.mineinabyss.protocolburrito.WrappedCompanion
import com.mineinabyss.protocolburrito.WrappedPacket
import com.squareup.kotlinpoet.*
import net.minecraft.network.protocol.Packet
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

val OUTPUT_DIR = File("build/generated/burrito/main/")

fun generateProtocolExtensions() {
    val reflections = Reflections("net.minecraft.network.protocol.game", SubTypesScanner(false))
    reflections.getSubTypesOf(Packet::class.java)
        .map { it.kotlin }
        .filter { !it.isAbstract }
        .forEach { packetClass ->
            val indices = mutableMapOf<KType, AtomicInteger>()
            val props = packetClass.declaredMemberProperties.mapNotNull { field ->
                val type = field.returnType
                if (field.visibility == KVisibility.PUBLIC) return@mapNotNull null
                if (field.returnType.jvmErasure.visibility != KVisibility.PUBLIC) return@mapNotNull null
                if (field.returnType.arguments.any { it.type?.jvmErasure?.visibility != KVisibility.PUBLIC }) return@mapNotNull null
                val index = indices.getOrPut(type) { AtomicInteger(0) }
                PropertySpec.builder(field.name, type.asTypeName().copy(annotations = listOf())) //.let {
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return %T.getField(handle, ${index.toInt()})", FieldHelpers::class)
                            .build()
                    )
                    .setter(
                        FunSpec.setterBuilder()
                            .addParameter(ParameterSpec.builder("value", type.asTypeName()).build())
                            .addCode("%T.setField(handle, ${index.toInt()}, value)", FieldHelpers::class)
                            .build()
                    )
                    .mutable(true)
                    .build().also {
                        index.getAndAdd(1)
                    }
            }

            val className = packetClass.simpleName + "Wrap"
            val file = FileSpec.builder("com.mineinabyss.protocolburrito.packets", className)
                .addType(
                    TypeSpec
                        .classBuilder(className)
                        .addSuperinterface(WrappedPacket::class)
                        .primaryConstructor(
                            // We specify any type to avoid issues if class names change
                            // Instead we get a packet from ProtocolLib's PacketType which should be
                            // consistent across versions.
                            PropertySpec.builder("handle", Any::class/*packetClass*/).build()
                        )
//                    .addProperty("handle", packetClass)
                        .addProperties(props)
                        .addType(
                            TypeSpec
                                .companionObjectBuilder()
                                .addSuperinterface(WrappedCompanion::class)
                                .addProperty(
                                    PropertySpec.builder("type", PacketType::class)
                                        .addModifiers(KModifier.OVERRIDE)
                                        .getter(
                                            FunSpec.getterBuilder()
                                                .addStatement(
                                                    "return %T.getPacketType(%T::class.java)",
                                                    PacketRegistry::class,
                                                    packetClass
                                                ).build()
                                        )
                                        .build()
                                )
                                .addFunction(
                                    FunSpec.builder("wrap")
                                        .addModifiers(KModifier.OVERRIDE)
                                        .addParameter("any", Any::class)
                                        .addStatement("return $className(any as %T)", packetClass)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .addFunction(
                    FunSpec.builder("wrap")
                        .receiver(packetClass)
                        .addModifiers(KModifier.INLINE)
                        .addStatement("return $className(this)")
                        .build()
                )
                .build()

            file.writeTo(OUTPUT_DIR)
        }
}

fun TypeSpec.Builder.primaryConstructor(vararg properties: PropertySpec): TypeSpec.Builder {
    val propertySpecs = properties.map { p -> p.toBuilder().initializer(p.name).build() }
    val parameters = propertySpecs.map { ParameterSpec.builder(it.name, it.type).build() }
    val constructor = FunSpec.constructorBuilder()
        .addParameters(parameters)
        .build()

    return this
        .primaryConstructor(constructor)
        .addProperties(propertySpecs)
}
