package com.mineinabyss.protocolburrito.dsl

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class BurritoProtocolManager(protocolManager: ProtocolManager, val plugin: Plugin) : ProtocolManager by protocolManager {
    fun ProtocolManager.onSend(
        vararg packets: PacketType,
        priority: ListenerPriority = ListenerPriority.NORMAL,
        onSend: PacketEvent.() -> Unit
    ) {
        addPacketListener(object : PacketAdapter(plugin, priority, *packets) {
            override fun onPacketSending(event: PacketEvent) {
                onSend(event)
            }
        })
    }

    fun ProtocolManager.onReceive(
            vararg packets: PacketType,
            priority: ListenerPriority = ListenerPriority.NORMAL,
            onSend: PacketEvent.() -> Unit
    ) {
        addPacketListener(object : PacketAdapter(plugin, priority, *packets) {
            override fun onPacketReceiving(event: PacketEvent) {
                onSend(event)
            }
        })
    }
}

fun protocolManager(plugin: Plugin, run: BurritoProtocolManager.() -> Unit) {
    BurritoProtocolManager(ProtocolLibrary.getProtocolManager()!!, plugin).apply(run)
}

fun PacketContainer.sendTo(player: Player) {
    ProtocolLibrary.getProtocolManager()!!.sendServerPacket(player, this)
}
