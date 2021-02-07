# ProtocolBurrito

A packet wrapper for ProtocolLib generated from [PrismarineJS/minecraft-data](https://github.com/PrismarineJS/minecraft-data) with clean Kotlin syntax and full Java support.

This project is currently HIGHLY WIP, and a lot will change! Don't use it yet but consider starring if this looks interesting to you.

### The goal

ProtocolLib is great, but it's extremely painful to actually modify data in packets because we need to access fields via integers and constantly refer to the [Protocol Wiki](https://wiki.vg/Protocol). This project allows you to wrap a packet and modify fields with actual names!

### Other projects

Though some other wrappers exist such as https://github.com/dmulloy2/PacketWrapper, it seems a bit outdated and designed for one-off use cases, recommending plugin authors to copy-paste classes.

We also think Kotlin's properties with getters and setters are perfect for a project like this and would like to make use of some more Kotlin features.

### What is minecraft-data

minecraft-data is a *"Language independent module providing minecraft data for minecraft clients, servers and libraries."* In essence it's a JSON representation of the Minecraft protocol among other things.

This project contains a generator which looks through these files for a specific version in minecraft-data and generates wrappers meant to be used alongside ProtocolLib.

### Usage

This project can be shaded into your plugin, however it's designed to be used as another plugin on your server (similar to ProtocolLib). Assuming there haven't been any name changes for a packet, this ensures your code stays version safe.

(TODO explain dependency setup and add download links)

### Limitations

- We currently require you to provide Kotlin yourself, either via shading or a separate dependency plugin. We are going to look into setting this up automatically with https://github.com/knightzmc/pdm/.
- Many data types outside primitives aren't supported. We'll be fixing this for ItemStacks soon but more complex NBT structure support will be harder to implement.
