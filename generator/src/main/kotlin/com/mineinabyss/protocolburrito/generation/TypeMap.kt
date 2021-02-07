package com.mineinabyss.protocolburrito.generation

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import java.util.*

object TypeMap {
    val types = mapOf(
        "varint" to Int::class,
//    "optvarint" to ::class,
//    "pstring" to ::class,
//    "u16" to Double::class,
        "u8" to Byte::class,
        "i64" to Long::class,
        "buffer" to ByteArray::class,
        "i32" to Int::class,
        "i8" to Byte::class,
        "bool" to Boolean::class,
        "i16" to Short::class,
        "f32" to Float::class,
        "f64" to Double::class,
        "UUID" to UUID::class,
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
        "string" to String::class,
        "slot" to ItemStack::class, //looks like ItemStack https://wiki.vg/Slot_Data
        "particle" to Particle::class, //TODO no idea if it's right
//    "particleData", //TODO no idea
//    "ingredient",
        "position" to Location::class,
//    "entityMetadataItem",
//    "entityMetadata" //https://wiki.vg/Entity_metadata#Entity_Metadata_Format
    )
}
