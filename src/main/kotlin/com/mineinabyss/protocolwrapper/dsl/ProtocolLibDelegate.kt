package com.mineinabyss.protocolwrapper.dsl

import com.comphenix.protocol.events.PacketContainer
import kotlin.reflect.KProperty

class ProtocolLibDelegate<T>(
    val index: Int,
    val type: Class<T>/*val modifier: PacketContainer.() -> StructureModifier<T>*/
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return (thisRef as PacketContainer).getSpecificModifier(type).read(index)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (thisRef is PacketContainer) thisRef.getSpecificModifier(type).write(index, value)
    }
}

//class IntDelegate(override val index: Int) : ProtocolLibDelegate<Int>({ integers }) {
//
//}
