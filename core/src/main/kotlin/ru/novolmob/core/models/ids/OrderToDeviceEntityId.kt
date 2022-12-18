package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class OrderToDeviceEntityId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<OrderToDeviceEntityId>, UUIDable {
    override fun compareTo(other: OrderToDeviceEntityId): Int = uuid.compareTo(other.uuid)
}