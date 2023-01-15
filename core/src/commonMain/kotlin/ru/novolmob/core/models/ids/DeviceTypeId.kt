package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class DeviceTypeId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceTypeId>, UUIDable {
    override fun compareTo(other: DeviceTypeId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): DeviceTypeId = DeviceTypeId(UUID.randomUUID())
        fun fromString(string: String): DeviceTypeId = DeviceTypeId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): DeviceTypeId = DeviceTypeId(UUID.nameUUIDFromBytes(array))
    }
}