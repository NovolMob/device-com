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
}
