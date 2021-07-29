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
}
