package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class DeviceTypeDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<DeviceTypeDetailId>, UUIDable {
    override fun compareTo(other: DeviceTypeDetailId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): DeviceTypeDetailId = DeviceTypeDetailId(UUID.randomUUID())
        fun fromString(string: String): DeviceTypeDetailId = DeviceTypeDetailId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): DeviceTypeDetailId = DeviceTypeDetailId(UUID.nameUUIDFromBytes(array))
    }
}