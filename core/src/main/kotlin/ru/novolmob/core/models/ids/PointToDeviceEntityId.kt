package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class PointToDeviceEntityId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<PointToDeviceEntityId>, UUIDable {
    override fun compareTo(other: PointToDeviceEntityId): Int = uuid.compareTo(other.uuid)
}