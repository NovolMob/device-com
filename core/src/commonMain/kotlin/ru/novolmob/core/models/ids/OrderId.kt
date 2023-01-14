package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class OrderId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderId>, UUIDable {
    override fun compareTo(other: OrderId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): OrderId = OrderId(UUID.randomUUID())
        fun fromString(string: String): OrderId = OrderId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): OrderId = OrderId(UUID.nameUUIDFromBytes(array))
    }
}