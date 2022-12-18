package ru.novolmob.database.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class DeviceTypeId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceTypeId>, UUIDable {
    override fun compareTo(other: DeviceTypeId): Int = uuid.compareTo(other.uuid)
}