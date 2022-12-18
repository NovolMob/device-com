package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class DeviceDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceDetailId>, UUIDable {
    override fun compareTo(other: DeviceDetailId): Int = uuid.compareTo(other.uuid)
}