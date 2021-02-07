package com.mineinabyss.protocolburrito

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

abstract class WrappedPacket(
    val handle: PacketContainer,
    val packetType: PacketType? = null,
) {
    init {
        require(packetType == null || handle.type == packetType) { "${handle.handle} is not a packet of type $packetType" }
    }

    inline fun <reified T> read(index: Int) = handle.getSpecificModifier(T::class.java).read(index)
    inline fun <reified T> write(index: Int, value: T) = handle.getSpecificModifier(T::class.java).write(index, value)
}
