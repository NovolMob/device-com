package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class WorkerId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<WorkerId>, UUIDable {
    override fun compareTo(other: WorkerId): Int = uuid.compareTo(other.uuid)
}