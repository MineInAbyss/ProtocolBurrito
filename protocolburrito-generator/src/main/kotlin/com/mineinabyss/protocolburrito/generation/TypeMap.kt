package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.StructureModifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction1

object TypeMap {
    class TypeInfo<T : Any>(
        val kClass: KClass<T>,
        val structureModified: KFunction1<PacketContainer, StructureModifier<T>>
    )

    inline fun <reified T : Any> get(function: KFunction1<PacketContainer, StructureModifier<T>>): TypeInfo<T> {
        return TypeInfo(T::class, function)
    }

    val types = mapOf<String, TypeInfo<*>>(
        "varint" to get(PacketContainer::getIntegers),
//    "optvarint" to ::class,
//    "pstring" to ::class,
//    "u16" to Double::class,
        "u8" to get(PacketContainer::getBytes),
        "i64" to get(PacketContainer::getLongs),
        "buffer" to get(PacketContainer::getByteArrays),
        "i32" to get(PacketContainer::getIntegers),
        "i8" to get(PacketContainer::getBytes),
        "bool" to get(PacketContainer::getBooleans),
        "i16" to get(PacketContainer::getShorts),
        "f32" to get(PacketContainer::getFloat),
        "f64" to get(PacketContainer::getDoubles),
        "UUID" to get(PacketContainer::getUUIDs),
//    "option" to ::class,
//    "entityMetadataLoop" to ::class,
//    "topBitSetTerminatedArray" to ::class, //used by packet_entity_equipment
//    "bitfield" to ::class, //used by packet_multi_block_change as chunk selection position
//    "container" to ::class, //used by packet_close_window as window id
//    "switch" to ::class,
//    "void" to ::class,
//    "array" to ::class, //TODO yeah this one'll be fun
//    "restBuffer" to ::class, //some nbt data for block lighting
//    "nbt" to NbtBase::class, //no idea how to read this properly
//    "optionalNbt" to ::class,
        "string" to get(PacketContainer::getStrings),
        "slot" to get(PacketContainer::getItemModifier), //looks like ItemStack https://wiki.vg/Slot_Data
        "particle" to get(PacketContainer::getParticles), //TODO no idea if it's right
//    "particleData", //TODO no idea
//    "ingredient",
        "position" to get(PacketContainer::getBlockPositionModifier),
//    "entityMetadataItem",
//    "entityMetadata" //https://wiki.vg/Entity_metadata#Entity_Metadata_Format
    )
}
