package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class GrantedRightId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<GrantedRightId>, UUIDable {
    override fun compareTo(other: GrantedRightId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()
}