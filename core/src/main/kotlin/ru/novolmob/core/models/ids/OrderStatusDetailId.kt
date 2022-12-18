package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class OrderStatusDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderStatusDetailId>, UUIDable {
    override fun compareTo(other: OrderStatusDetailId): Int = uuid.compareTo(other.uuid)
}