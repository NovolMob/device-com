package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class PointId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<PointId>, UUIDable {
    override fun compareTo(other: PointId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): PointId = PointId(UUID.randomUUID())
        fun fromString(string: String): PointId = PointId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): PointId = PointId(UUID.nameUUIDFromBytes(array))
    }

}