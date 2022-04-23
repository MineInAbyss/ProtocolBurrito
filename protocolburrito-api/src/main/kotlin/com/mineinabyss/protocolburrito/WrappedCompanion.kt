package com.mineinabyss.protocolburrito

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.injector.packet.PacketRegistry

interface WrappedCompanion {
    val type: PacketType

    fun wrap(any: Any): Any
}
