package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class OrderToDeviceEntityId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderToDeviceEntityId>, UUIDable {
    override fun compareTo(other: OrderToDeviceEntityId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): OrderToDeviceEntityId = OrderToDeviceEntityId(UUID.randomUUID())
        fun fromString(string: String): OrderToDeviceEntityId = OrderToDeviceEntityId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): OrderToDeviceEntityId = OrderToDeviceEntityId(UUID.nameUUIDFromBytes(array))
    }
}