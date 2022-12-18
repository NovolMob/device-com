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
}