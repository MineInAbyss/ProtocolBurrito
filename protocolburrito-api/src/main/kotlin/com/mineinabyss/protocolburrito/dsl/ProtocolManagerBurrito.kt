package com.mineinabyss.protocolburrito.dsl

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.injector.packet.PacketRegistry
import com.mineinabyss.protocolburrito.WrappedCompanion
import com.mineinabyss.protocolburrito.WrappedPacket
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.reflect.KFunction1
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance

class ProtocolManagerBurrito(
    val protocolManager: ProtocolManager,
    val plugin: Plugin
) : ProtocolManager by protocolManager {
    inline fun  <reified T: WrappedPacket> onSend(
        priority: ListenerPriority = ListenerPriority.NORMAL,
        crossinline onSend: PacketEvent.(wrapped: T) -> Unit
    ) {
        val companion = T::class.companionObjectInstance as WrappedCompanion
        onSend(companion.packetType, priority = priority) { onSend(companion.wrap(packet.handle) as T) }
    }

    inline fun onSend(
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

    inline fun <reified T> onReceive(
        priority: ListenerPriority = ListenerPriority.NORMAL,
        crossinline onReceive: PacketEvent.(wrapped: T) -> Unit
    ) {
        val companion = T::class.companionObjectInstance as WrappedCompanion
        onReceive(companion.packetType, priority = priority) { onReceive(companion.wrap(packet.handle) as T) }
    }

    inline fun onReceive(
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
