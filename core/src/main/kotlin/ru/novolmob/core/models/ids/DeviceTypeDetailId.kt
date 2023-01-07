package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class DeviceTypeDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceTypeDetailId>, UUIDable {
    override fun compareTo(other: DeviceTypeDetailId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()
}