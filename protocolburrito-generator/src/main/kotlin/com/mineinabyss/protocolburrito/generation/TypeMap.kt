package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.StructureModifier
import org.bukkit.entity.Entity
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
        "byte" to get(PacketContainer::getBytes),
        "boolean" to get(PacketContainer::getBooleans),
        "short" to get(PacketContainer::getShorts),
        "int" to get(PacketContainer::getIntegers),
        "long" to get(PacketContainer::getLongs),
        "float"  to get(PacketContainer::getFloat),
        "double"  to get(PacketContainer::getDoubles),
        "java.lang.String" to get(PacketContainer::getStrings),
        "java.util.UUID" to get(PacketContainer::getUUIDs),
        //TODO doesnt work because Array<String> gets type erased
//        "java.lang.String[]" to get<Array<String>>(PacketContainer::getStringArrays),
        "byte[]" to get<ByteArray>(PacketContainer::getByteArrays),
        "int[]" to get<IntArray>(PacketContainer::getIntegerArrays),
        "short[]" to get<ShortArray>(PacketContainer::getShortArrays),
//        "" to get(PacketContainer::getItemModifier),
//        "" to get(PacketContainer::getItemArrayModifier),
//        "" to get(PacketContainer::getItemListModifier),
//        "" to get(PacketContainer::getStatisticMaps),
//        "" to get(PacketContainer::getWorldTypeModifier),
//        "" to get(PacketContainer::getDataWatcherModifier),
        "net.minecraft.world.entity.EntityType" to get(PacketContainer::getEntityTypeModifier),
        "net.minecraft.core.BlockPos" to get(PacketContainer::getBlockPositionModifier),
        //TODO UNSURE
        "net.minecraft.network.chat.Component" to get(PacketContainer::getChatComponents),
        //TODO figure something out with this modifier, requires world parameter pass
//        "net.minecraft.world.entity.Entity" to get(PacketContainer::getEntityModifier),
    )
}
