package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class PointDetailId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<PointDetailId>, UUIDable {
    override fun compareTo(other: PointDetailId): Int = uuid.compareTo(other.uuid)
}