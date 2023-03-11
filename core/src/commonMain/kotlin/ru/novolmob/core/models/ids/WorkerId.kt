package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class WorkerId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<WorkerId>, UUIDable {
    override fun compareTo(other: WorkerId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): WorkerId = WorkerId(UUID.randomUUID())
        fun fromString(string: String): WorkerId = WorkerId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): WorkerId = WorkerId(UUID.nameUUIDFromBytes(array))
    }

}