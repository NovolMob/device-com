package ru.novolmob.database.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class PointToDeviceEntityId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<PointToDeviceEntityId>, UUIDable {
    override fun compareTo(other: PointToDeviceEntityId): Int = uuid.compareTo(other.uuid)
}