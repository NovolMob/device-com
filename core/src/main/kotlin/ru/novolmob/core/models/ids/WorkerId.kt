package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class WorkerId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<WorkerId>, UUIDable {
    override fun compareTo(other: WorkerId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()
}