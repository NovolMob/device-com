package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class OrderToStatusEntityId(
    @Serializable(UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderToStatusEntityId>, UUIDable {
    override fun compareTo(other: OrderToStatusEntityId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): OrderToStatusEntityId = OrderToStatusEntityId(UUID.randomUUID())
        fun fromString(string: String): OrderToStatusEntityId = OrderToStatusEntityId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): OrderToStatusEntityId = OrderToStatusEntityId(UUID.nameUUIDFromBytes(array))
    }
}
