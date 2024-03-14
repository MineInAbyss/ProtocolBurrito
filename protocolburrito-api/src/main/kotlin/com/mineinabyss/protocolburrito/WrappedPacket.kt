package com.mineinabyss.protocolburrito

import com.comphenix.protocol.events.PacketContainer

interface WrappedPacket {
    val container: PacketContainer
}
