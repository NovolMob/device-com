package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class OrderStatusId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderStatusId>, UUIDable {
    override fun compareTo(other: OrderStatusId): Int = uuid.compareTo(other.uuid)
}