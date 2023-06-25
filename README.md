<div align="center">

# ProtocolBurrito

[![Package](https://img.shields.io/maven-metadata/v?metadataUrl=https://repo.mineinabyss.com/releases/com/mineinabyss/protocolburrito/maven-metadata.xml)](https://repo.mineinabyss.com/#/releases/com/mineinabyss/protocolburrito)
[![Contribute](https://shields.io/badge/Contribute-e57be5?logo=github%20sponsors&style=flat&logoColor=white)](https://github.com/MineInAbyss/MineInAbyss/wiki/Setup-and-Contribution-Guide)
</div>

A packet wrapper for ProtocolLib generated from Minecraft's obfuscation mappings, thanks to PaperMC's [userdev](https://github.com/PaperMC/paperweight/tree/main/paperweight-userdev) project. Comes with clean Kotlin syntax and Java support.

This project is currently **HIGHLY WIP**! Don't use it if you are a fan of stable codebases.

## The goal

ProtocolLib is great, but it's extremely painful to actually modify data in packets because we need to access fields via integers and constantly refer to the [Protocol Wiki](https://wiki.vg/Protocol). This project takes the server packet classes and exposes all their private fields by accessing them with integers. The [userdev](https://github.com/PaperMC/paperweight/tree/main/paperweight-userdev) Gradle plugin lets you work directly with the data and avoid messy ProtocolLib code.

### Costs

You lose on some version safety by depending on NMS code. However, with the new mappings, updating is far easier and the code for complex operations more maintainable.

## Kotlin DSL

We also provide a clean Kotlin DSL wrapper around ProtocolLib's packet interceptors, plus some extension functions for sending packets, or getting an entity from its id.

## Usage

This project can be shaded into your plugin, however it's designed to be used as another plugin on your server (similar to ProtocolLib).

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
        if (Bukkit.getEntity(event.packet.uuiDs.read(0)).isCustomMob)
            event.packet.integers.write(1, 102)
    }
})
```

##### After

```kotlin
protocolManager(pluginRef) {
    onSend<ClientboundAddEntityPacketWrap> { wrap ->
        if (entity(wrap.id).isCustomMob)
            wrap.type = Registry.ENTITY_TYPE.getId(NMSEntityType.ZOMBIE)
    }
}
```

### Sending packets

##### An example from ProtocolLib

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

##### And the ProtocolBurrito equivalent

```kotlin
val loc = player.location
// The NMS class already has a public constructor!
val fakeExplosion = ClientboundExplodePacket(loc.x, loc.y, loc.z, 3.0f, listOf(), null)
fakeExplosion.sendTo(player) 
```

And if the constructor is too complicated, you can fall back to:

```kotlin
val fakeExplosion = PacketContainer(PacketType.Play.Server.EXPLOSION)
(fakeExplosion.handle as ClientboundExplodePacket).wrap().apply {
    x = loc.x
    y = loc.y
    z = loc.z
    power = 3.0f
}
fakeExplosion.sendTo(player) 
```

We lost some version safety, but the increased readability ensures future updates will be far easier when something in the packet inevitably changes, no more looking up integers!

### Kotlin runtime

We're currently trying to make this seamless.
