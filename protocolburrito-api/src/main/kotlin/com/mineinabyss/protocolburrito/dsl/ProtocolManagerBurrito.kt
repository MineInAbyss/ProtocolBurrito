package com.mineinabyss.protocolburrito.dsl

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class ProtocolManagerBurrito(
    val protocolManager: ProtocolManager,
    val plugin: Plugin
) : ProtocolManager by protocolManager {
    inline fun ProtocolManager.onSend(
        vararg packets: PacketType,
        priority: ListenerPriority = ListenerPriority.NORMAL,
        crossinline onSend: PacketEvent.() -> Unit
    ) {
        addPacketListener(object : PacketAdapter(plugin, priority, *packets) {
            override fun onPacketSending(event: PacketEvent) {
                onSend(event)
            }
        })
    }

    inline fun ProtocolManager.onReceive(
        vararg packets: PacketType,
        priority: ListenerPriority = ListenerPriority.NORMAL,
        crossinline onSend: PacketEvent.() -> Unit
    ) {
        addPacketListener(object : PacketAdapter(plugin, priority, *packets) {
            override fun onPacketReceiving(event: PacketEvent) {
                onSend(event)
            }
        })
    }

    fun PacketEvent.entity(entityId: Int): Entity =
        getEntityFromID(player.world, entityId)

    fun entity(uuid: UUID): Entity? =
        Bukkit.getEntity(uuid)

    fun PacketContainer.receive(player: Player) {
        protocolManager.sendServerPacket(player, this)
    }
}

inline fun protocolManager(plugin: Plugin, run: ProtocolManagerBurrito.() -> Unit) {
    ProtocolManagerBurrito(ProtocolLibrary.getProtocolManager()!!, plugin).apply(run)
}

fun PacketContainer.sendTo(player: Player) {
    ProtocolLibrary.getProtocolManager()!!.sendServerPacket(player, this)
}
