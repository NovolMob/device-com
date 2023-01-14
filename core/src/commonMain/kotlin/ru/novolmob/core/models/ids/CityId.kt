package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class CityId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<CityId>, UUIDable {
    override fun compareTo(other: CityId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): CityId = CityId(UUID.randomUUID())
        fun fromString(string: String): CityId = CityId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): CityId = CityId(UUID.nameUUIDFromBytes(array))
    }
}