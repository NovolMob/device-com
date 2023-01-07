package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class BasketId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<BasketId>, UUIDable {
    override fun compareTo(other: BasketId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()
}