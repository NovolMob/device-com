package ru.novolmob.database.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class DeviceId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceId>, UUIDable {
    override fun compareTo(other: DeviceId): Int = uuid.compareTo(other.uuid)
}