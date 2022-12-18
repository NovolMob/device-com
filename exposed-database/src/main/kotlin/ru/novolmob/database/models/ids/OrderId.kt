package ru.novolmob.database.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class OrderId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderId>, UUIDable {
    override fun compareTo(other: OrderId): Int = uuid.compareTo(other.uuid)
}