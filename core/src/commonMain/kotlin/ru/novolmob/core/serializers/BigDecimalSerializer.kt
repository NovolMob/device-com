package ru.novolmob.core.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.novolmob.core.models.BigDecimal
import ru.novolmob.core.models.BigDecimal.Companion.toBigDecimal

object BigDecimalSerializer: KSerializer<BigDecimal> {
    override fun deserialize(decoder: Decoder): BigDecimal =
        decoder.decodeString().toBigDecimal()
    override fun serialize(encoder: Encoder, value: BigDecimal) =
        encoder.encodeString(value.toPlainString())
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)
}