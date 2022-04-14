package com.mineinabyss.protocolburrito

object FieldHelpers {
    fun <T : Any> getField(forObject: Any, type: Class<T>, index: Int): T {
        var id = 0
        return forObject::class.java.fields.first { it.type.kotlin == type && index == id++ }.get(forObject) as T
    }

    fun <T : Any> setField(forObject: Any, type: Class<T>, index: Int, value: T) {
        var id = 0
        forObject::class.java.fields.first { it.type == type && index == id++ }.set(forObject, value)
    }
}
