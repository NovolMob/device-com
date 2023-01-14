package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class UserId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<UserId>, UUIDable {
    override fun compareTo(other: UserId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): UserId = UserId(UUID.randomUUID())
        fun fromString(string: String): UserId = UserId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): UserId = UserId(UUID.nameUUIDFromBytes(array))
    }

}