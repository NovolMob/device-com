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
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): DeviceDetailId = DeviceDetailId(UUID.randomUUID())
        fun fromString(string: String): DeviceDetailId = DeviceDetailId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): DeviceDetailId = DeviceDetailId(UUID.nameUUIDFromBytes(array))
    }
}