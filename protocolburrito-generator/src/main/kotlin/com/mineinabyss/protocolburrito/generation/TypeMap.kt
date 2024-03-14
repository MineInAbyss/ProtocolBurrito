package com.mineinabyss.protocolburrito.generation

import com.comphenix.protocol.events.PacketContainer
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.stats.Stat
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.full.withNullability

object TypeMap {
    class TypeInfo(
        val type: TypeName,
        val structureModifierMember: MemberName
//        val structureModifier: KFunction1<PacketContainer, StructureModifier<T>>
    )

//    inline fun <reified T : Any> get(function: KFunction1<PacketContainer, StructureModifier<T>>): TypeInfo<T> {
//        return TypeInfo(T::class, function)
//    }
//
//    val types = mapOf<String, TypeInfo>(
//        // Arrays
//        //TODO doesnt work because Array<String> gets type erased
////        "java.lang.String[]" to get<Array<String>>(PacketContainer::getStringArrays),
//        "byte[]" to get<ByteArray>(PacketContainer::getByteArrays),
//        "int[]" to get<IntArray>(PacketContainer::getIntegerArrays),
//        "short[]" to get<ShortArray>(PacketContainer::getShortArrays),
//        "net.minecraft.world.entity.EntityType<?>" to get(PacketContainer::getEntityTypeModifier),
//        "net.minecraft.core.BlockPos" to get(PacketContainer::getBlockPositionModifier),
//        //TODO UNSURE
//        "net.minecraft.network.chat.Component" to get(PacketContainer::getChatComponents),
//        //TODO figure something out with this modifier, requires world parameter pass
////        "net.minecraft.world.entity.Entity" to get(PacketContainer::getEntityModifier),
//    )

    @OptIn(ExperimentalStdlibApi::class)
    fun getStructureModifier(type: KType): TypeInfo? {
//        val type = type.withNullability(false)
//        val singleArgument = type.arguments.singleOrNull()?.type?.withNullability(false)
//        if(singleArgument != null) {
//            val outerType = outerModifier[type.jvmErasure]
//            println("Working on $type with args ${type.arguments}, and erased ${type.jvmErasure}")
//            if (outerType != null) {
//                println(FunSpec.builder("test").addStatement("val test = %N", outerType).build())
//            }
//
//            leafModifier[singleArgument]
//
//            println("Got type $outerType")
//        }
        val modifier = mappedLeafModifiers[type.javaType] ?: return null
        val structureModifierName = modifier.name
        val fieldType = modifier.returnType.arguments.single().type!!.asTypeName()

        return TypeInfo(fieldType, packetContainer.member(structureModifierName))
    }

    val packetContainer = PacketContainer::class.asClassName()

    val outerModifier = mapOf<KClass<*>, MemberName>(
        List::class to packetContainer.member("lists"),
//        Optional::class to PacketContainer::getOptionals,
    )

    private val leafModifier = mapOf<KType, KFunction<*>>(
        // Primitives
        typeOf<Byte>() to PacketContainer::getIntegers,
        typeOf<Boolean>() to PacketContainer::getBooleans,
        typeOf<Short>() to PacketContainer::getShorts,
        typeOf<Int>() to PacketContainer::getIntegers,
        typeOf<Long>() to PacketContainer::getLongs,
        typeOf<Float>() to PacketContainer::getFloat,
        typeOf<Double>() to PacketContainer::getDoubles,
        typeOf<String>() to PacketContainer::getStrings,
        typeOf<UUID>() to PacketContainer::getUUIDs,
        typeOf<Array<String>>() to PacketContainer::getStringArrays,
        typeOf<ByteArray>() to PacketContainer::getByteArrays,
        typeOf<IntArray>() to PacketContainer::getIntegerArrays,
        typeOf<ShortArray>() to PacketContainer::getShortArrays,
        typeOf<net.minecraft.world.item.ItemStack>() to PacketContainer::getItemModifier,
        typeOf<Array<net.minecraft.world.item.ItemStack>>() to PacketContainer::getItemArrayModifier,
        typeOf<List<net.minecraft.world.item.ItemStack>>() to PacketContainer::getItemListModifier,
        typeOf<Object2IntMap<Stat<*>>>() to PacketContainer::getStatisticMaps,
//        typeOf<net.minecraft.world.level.GameType>() to PacketContainer::getWorldTypeModifier,
        typeOf<net.minecraft.network.syncher.SynchedEntityData>() to PacketContainer::getDataWatcherModifier,
        typeOf<net.minecraft.world.entity.EntityType<*>>() to PacketContainer::getEntityTypeModifier,
        typeOf<net.minecraft.core.BlockPos>() to PacketContainer::getBlockPositionModifier,
        typeOf<net.minecraft.world.level.block.entity.BlockEntityType<*>>() to PacketContainer::getBlockEntityTypeModifier,
        // TODO getChunkCoordIntPairs
        typeOf<net.minecraft.nbt.CompoundTag>() to PacketContainer::getNbtModifier,
        typeOf<List<net.minecraft.nbt.CompoundTag>>() to PacketContainer::getListNbtModifier,
        typeOf<net.minecraft.world.phys.Vec3>() to PacketContainer::getVectors,
        typeOf<List<net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket.AttributeSnapshot>>() to PacketContainer::getAttributeCollectionModifier,
        typeOf<List<net.minecraft.core.BlockPos>>() to PacketContainer::getBlockPositionCollectionModifier,
        typeOf<List<net.minecraft.network.syncher.SynchedEntityData.DataItem<*>>>() to PacketContainer::getWatchableCollectionModifier,
        typeOf<List<net.minecraft.network.syncher.SynchedEntityData.DataValue<*>>>() to PacketContainer::getDataValueCollectionModifier,
        // blocks
        typeOf<net.minecraft.world.level.block.Block>() to PacketContainer::getBlocks,
        typeOf<net.minecraft.world.level.block.state.BlockState>() to PacketContainer::getBlockData,
        typeOf<Array<net.minecraft.world.level.block.state.BlockState>>() to PacketContainer::getBlockDataArrays,
        //getMultiBlockChangeInfoArrays
        typeOf<net.minecraft.network.chat.Component>() to PacketContainer::getChatComponents,
        typeOf<Array<net.minecraft.network.chat.Component>>() to PacketContainer::getChatComponentArrays,
        // getServerPings
        // getPlayerInfoDataLists
        // getProtocols
        // getClientCommands
        // getChatVisibilities
        typeOf<net.minecraft.world.Difficulty>() to PacketContainer::getDifficulties,
        // getEntityUseActions
        // getEnumEntityUseActions
        // getGameModes
        // getResourcePackStatus
        // getPlayerInfoAction
        // getPlayerInfoActions
        // getTitleActions
        // getWorldBorderActions
        // getCombatEvents
        // getPlayerDigTypes
        // getPlayerActions
        // getScoreboardActions
        // getParticles
        // getNewParticles
        // getEffectTypes
        // getSoundCategories
        // ...
        typeOf<net.minecraft.world.InteractionHand>() to PacketContainer::getHands,
        typeOf<net.minecraft.core.Direction>() to PacketContainer::getDirections,
        typeOf<net.minecraft.network.chat.ChatType.BoundNetwork>() to PacketContainer::getChatTypes,
        typeOf<net.minecraft.resources.ResourceLocation>() to PacketContainer::getMinecraftKeys,
        // getDimensions
        typeOf<net.minecraft.world.level.dimension.DimensionType>() to PacketContainer::getDimensionTypes,
        // getMerchantRecipeLists
        // ...
    )
    @OptIn(ExperimentalStdlibApi::class)
    private val mappedLeafModifiers = leafModifier.mapKeys { it.key.javaType }
}
