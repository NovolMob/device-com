package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class PointDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<PointDetailId>, UUIDable {
    override fun compareTo(other: PointDetailId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): PointDetailId = PointDetailId(UUID.randomUUID())
        fun fromString(string: String): PointDetailId = PointDetailId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): PointDetailId = PointDetailId(UUID.nameUUIDFromBytes(array))
    }

}