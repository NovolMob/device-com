package ru.novolmob.core.models.ids

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.UUIDSerializer
import java.util.UUID

@JvmInline
@Serializable
value class CredentialId(
    @Serializable(with = UUIDSerializer::class)
    override val uuid: UUID
): Comparable<CredentialId>, UUIDable {
    override fun compareTo(other: CredentialId): Int = uuid.compareTo(other.uuid)
    override fun toString(): String = uuid.toString()

    companion object {
        fun random(): CredentialId = CredentialId(UUID.randomUUID())
        fun fromString(string: String): CredentialId = CredentialId(UUID.fromString(string))
        fun fromBytes(array: ByteArray): CredentialId = CredentialId(UUID.nameUUIDFromBytes(array))
    }
}