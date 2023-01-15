package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class GrantedRightId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<GrantedRightId>, UUIDable {
    override fun compareTo(other: GrantedRightId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): GrantedRightId = GrantedRightId(UUID.randomUUID())
        fun fromString(string: String): GrantedRightId = GrantedRightId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): GrantedRightId = GrantedRightId(UUID.nameUUIDFromBytes(array))
    }
}