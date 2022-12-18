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
}