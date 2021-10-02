[![Java CI with Gradle](https://github.com/MineInAbyss/ProtocolBurrito/actions/workflows/gradle-ci.yml/badge.svg)](https://github.com/MineInAbyss/ProtocolBurrito/actions/workflows/gradle-ci.yml)
[![Package](https://badgen.net/maven/v/metadata-url/repo.mineinabyss.com/releases/com/mineinabyss/protocolburrito/maven-metadata.xml)](https://repo.mineinabyss.com/releases/com/mineinabyss/protocolburrito)
[![Contribute](https://shields.io/badge/Contribute-e57be5?logo=github%20sponsors&style=flat&logoColor=white)](https://github.com/MineInAbyss/MineInAbyss/wiki/Setup-and-Contribution-Guide)

# ProtocolBurrito

A packet wrapper for ProtocolLib generated from [PrismarineJS/minecraft-data](https://github.com/PrismarineJS/minecraft-data) with clean Kotlin syntax and full Java support.

This project is currently **HIGHLY WIP**, and a lot will change! Don't use it yet as there are sometimes inconsistencies where packets aren't modified correctly. Please consider starring if this looks interesting to you.

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

### Gradle

```groovy
repositories {
    maven  { url 'https://repo.mineinabyss.com/releases' }
}

dependencies {
    compileOnly 'com.mineinabyss:protocolburrito:<version>'
}
```

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
    onSend(PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
        PacketSpawnEntityLiving(packet).apply {
            if (entity(entityUUID)?.isCustomEntity == true)
                type = PacketEntityType.ZOMBIE.id
        }
    }
}
```

### Sending packets

Normally, you might have to do something ugly like the following:

````java
PacketContainer fakeExplosion = new PacketContainer(PacketType.Play.Server.EXPLOSION);
fakeExplosion.getDoubles().
    write(0, player.getLocation().getX()).
    write(1, player.getLocation().getY()).
    write(2, player.getLocation().getZ());
fakeExplosion.getFloat().write(0, 3.0F);

try {
    protocolManager.sendServerPacket(player, fakeExplosion);
} catch (InvocationTargetException e) {
    throw new RuntimeException(
        "Cannot send packet " + fakeExplosion, e);
}
````

But with ProtocolBurrito, you can turn that into something more manageable:

````kotlin
val fakeExplosion = ClientboundExplodePacket(PacketContainer(PacketType.Play.Server.EXPLOSION))
fakeExplosion.x = player.location.x
fakeExplosion.y = player.location.y
fakeExplosion.z = player.location.z

fakeExplosion.power = 3.0f

fakeExplosion.handle.sendTo(player)
````

### Kotlin runtime

We use [pdm](https://github.com/knightzmc/pdm/) to download the Kotlin stdlib on the server. You do not need to install anything else and other plugins using pdm will use the same downloaded file. 

### Limitations

- Many data types (including arrays) aren't currently supported. These values are simply skipped, and a wrapper won't be generated for them.
- In some edge cases the ids for the wrapper don't properly match the packet. These are issues with how ProtocolLib uses reflection to modify fields in NMS, we are considering writing our own packet interceptor that will work 100% of the time.
