package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class DeviceId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceId>, UUIDable {
    override fun compareTo(other: DeviceId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): DeviceId = DeviceId(UUID.randomUUID())
        fun fromString(string: String): DeviceId = DeviceId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): DeviceId = DeviceId(UUID.nameUUIDFromBytes(array))
    }
}