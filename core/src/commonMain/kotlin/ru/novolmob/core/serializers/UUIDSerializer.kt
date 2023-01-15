package ru.novolmob.core.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.novolmob.core.models.ids.UUID

object UUIDSerializer: KSerializer<UUID> {
    override fun deserialize(decoder: Decoder): UUID =
        UUID.fromString(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: UUID) =
        encoder.encodeString(value.toString())
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Uuid", PrimitiveKind.STRING)
}