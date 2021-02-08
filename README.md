# ProtocolBurrito

A packet wrapper for ProtocolLib generated from [PrismarineJS/minecraft-data](https://github.com/PrismarineJS/minecraft-data) with clean Kotlin syntax and full Java support.

This project is currently HIGHLY WIP, and a lot will change! Don't use it yet but consider starring if this looks interesting to you.

## The goal

ProtocolLib is great, but it's extremely painful to actually modify data in packets because we need to access fields via integers and constantly refer to the [Protocol Wiki](https://wiki.vg/Protocol). This project allows you to wrap a packet and modify fields with actual names!

### What is minecraft-data

minecraft-data is a *"Language independent module providing minecraft data for minecraft clients, servers and libraries."* In essence it's a JSON representation of the Minecraft protocol among other things.

This project contains a generator which looks through these files for a specific version in minecraft-data and generates wrappers meant to be used alongside ProtocolLib.

### Other projects

Though some other wrappers exist such as https://github.com/dmulloy2/PacketWrapper, it seems a bit outdated and designed for one-off use cases, recommending plugin authors to copy-paste classes.

We also think Kotlin's properties with getters and setters are perfect for a project like this and would like to make use of some more Kotlin features.

## Kotlin DSL

We also provide a clean Kotlin DSL wrapper around ProtocolLib's packet interceptors, plus some extension functions for sending packets. 

## Usage

This project can be shaded into your plugin, however it's designed to be used as another plugin on your server (similar to ProtocolLib). Assuming there haven't been any name changes for a packet, this ensures your code stays version safe.

(TODO explain dependency setup and add download links)

### Example

*Super not finalized but should give you a general idea of what the project does! We'll get some java examples in here once things are more stable.*

This is a snippet of code sends zombie as entity type for our custom mobs from another project:

##### Before

```kotlin
val protocolManager = ProtocolLibrary.getProtocolManager()!!

protocolManager.addPacketListener(object : PacketAdapter(
    pluginRef,
    ListenerPriority.NORMAL,
    PacketType.Play.Server.SPAWN_ENTITY_LIVING
) {
    override fun onPacketSending(event: PacketEvent) {
        if (Bukkit.getEntity(event.packet.uuiDs.read(0))?.isCustomMob == true)
            event.packet.integers.write(1, 102)
    }
})
```

##### After

```kotlin
protocolManager(pluginRef) {
    onSend(Server.SPAWN_ENTITY_LIVING) {
        PacketSpawnEntityLiving(packet).apply {
            if (entity(entityUUID)?.isCustomEntity == true)
                type = PacketEntityType.ZOMBIE.id
        }
    }
}
```

### Limitations

- We currently require you to provide Kotlin yourself, either via shading or a separate dependency plugin. We are going to look into setting this up automatically with https://github.com/knightzmc/pdm/.
- Many data types (including arrays) aren't currently supported. These values are simply skipped, and a wrapper won't be generated for them.
