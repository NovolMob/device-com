package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class PointId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<PointId>, UUIDable {
    override fun compareTo(other: PointId): Int = uuid.compareTo(other.uuid)
}