import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.nbt.NbtBase
import com.mineinabyss.protocolwrapper.dsl.WrappedPacket
import com.nfeld.jsonpathkt.JsonPath
import com.nfeld.jsonpathkt.extension.read
import com.squareup.kotlinpoet.*
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

val generated = File(System.getProperty("user.dir") + "/src/main/generated")

fun main() {
    val json = File("protocol.json").readText()
    val parsed: Map<String, List<Any?>> =
        JsonPath.parse(json)?.read("$.play.toClient.types")!! // returns mapOf("inner" to 1)

    parsed.filter { it.key != "packet" }
//        .filter { it.key == "packet_entity_move_look" }
        .forEach { entry ->
            runCatching {
                generateWrapper(
                    entry.key,
                    entry.value.dropWhile { it == "container" }
                        .filterIsInstance<List<HashMap<String, *>>>()
                        .first()
                )
            }.onFailure { println("Skipping $entry due to error") }
        }


}

val types = mapOf(
    "varint" to Int::class,
//    "optvarint" to ::class,
//    "pstring" to ::class,
//    "u16" to Double::class,
    "u8" to Byte::class,
    "i64" to Long::class,
    "buffer" to ByteArray::class,
    "i32" to Int::class,
    "i8" to Byte::class,
    "bool" to Boolean::class,
    "i16" to Short::class,
    "f32" to Float::class,
    "f64" to Double::class,
    "UUID" to UUID::class,
//    "option" to ::class,
//    "entityMetadataLoop" to ::class,
//    "topBitSetTerminatedArray" to ::class, //used by packet_entity_equipment
//    "bitfield" to ::class, //used by packet_multi_block_change as chunk selection position
//    "container" to ::class, //used by packet_close_window as window id
//    "switch" to ::class,
//    "void" to ::class,
//    "array" to ::class, //TODO yeah this one'll be fun
//    "restBuffer" to ::class, //some nbt data for block lighting
    "nbt" to NbtBase::class, //no idea how to read this properly
//    "optionalNbt" to ::class,
    "string" to String::class,
    "slot" to ItemStack::class, //looks like ItemStack https://wiki.vg/Slot_Data
    "particle" to Particle::class, //TODO no idea if it's right
//    "particleData", //TODO no idea
//    "ingredient",
    "position" to Location::class,
//    "entityMetadataItem",
//    "entityMetadata" //https://wiki.vg/Entity_metadata#Entity_Metadata_Format
)


fun getType(type: String): KClass<*> {
    return types[type] ?: Int::class
}

fun generateWrapper(packetName: String, params: List<HashMap<String, *>>) {
    val className = packetName.capitalize().replace("_", "")
//    println("${this["name"]} ${this["type"]}")

//    val typeClasses: List<KClass<*>> = params.toSet().mapNotNull {
//        types[it["type"]]
//    }

    val indices = mutableMapOf<String, AtomicInteger>()
    val props = params.map {
        val type = it["type"] as String
        val name = it["name"] as String

        val index = indices.getOrPut(type) { AtomicInteger(0) }

        val typeClass = getType(type)
        PropertySpec.builder(name, typeClass)

            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return read(${index.toInt()})").build()
            )
            .setter(
                FunSpec.setterBuilder()
                    .addParameter(ParameterSpec.builder("value", typeClass).build())
                    .addCode("write(${index.toInt()}, value)").build()
            )
            .mutable(true)
            .build().also {
                index.getAndAdd(1)
            }
    }

    val file = FileSpec.builder("", className)
        .addType(
            TypeSpec
                .classBuilder(className)
                .superclass(WrappedPacket::class)
                .addSuperclassConstructorParameter("handle")
                .primaryConstructor(
                    FunSpec
                        .constructorBuilder()
                        .addParameter("handle", PacketContainer::class)
                        .build()
                )
                .addProperties(props)
                .build()
        ).build()

    file.writeTo(generated)
}
