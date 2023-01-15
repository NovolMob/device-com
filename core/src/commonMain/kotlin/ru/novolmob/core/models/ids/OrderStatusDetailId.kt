package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class OrderStatusDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderStatusDetailId>, UUIDable {
    override fun compareTo(other: OrderStatusDetailId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): OrderStatusDetailId = OrderStatusDetailId(UUID.randomUUID())
        fun fromString(string: String): OrderStatusDetailId = OrderStatusDetailId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): OrderStatusDetailId = OrderStatusDetailId(UUID.nameUUIDFromBytes(array))
    }
}