package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class OrderStatusId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderStatusId>, UUIDable {
    override fun compareTo(other: OrderStatusId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): OrderStatusId = OrderStatusId(UUID.randomUUID())
        fun fromString(string: String): OrderStatusId = OrderStatusId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): OrderStatusId = OrderStatusId(UUID.nameUUIDFromBytes(array))
    }
}