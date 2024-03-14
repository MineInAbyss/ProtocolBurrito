package com.mineinabyss.protocolburrito

import com.comphenix.protocol.events.PacketContainer

object FieldHelpers {
    inline fun <reified T : Any> getField(forObject: Any, index: Int): T =
        getField(T::class.java, forObject, index)

    fun <T : Any> getField(type: Class<T>, forObject: Any, index: Int): T {
        var id = 0
        return forObject::class.java.declaredFields.first { it.type.kotlin.javaObjectType == type && index == id++ }
            .also {
                it.isAccessible = true
            }.get(forObject) as T
    }

    inline fun <reified T : Any> setField(forObject: Any, index: Int, value: T?) =
        setField(T::class.java, forObject, index, value)

    fun <T : Any> setField(type: Class<T>, forObject: Any, index: Int, value: T?) {
        var id = 0
        forObject::class.java.declaredFields.first { it.type.kotlin.javaObjectType == type && index == id++ }.also {
            it.isAccessible = true
        }.set(forObject, value)
    }
}
