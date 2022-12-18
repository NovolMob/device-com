package ru.novolmob.core.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.utils.PhoneNumberUtil

object PhoneNumberSerializer: KSerializer<PhoneNumber> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("PhoneNumber", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): PhoneNumber {
        return PhoneNumberUtil.deserializePhoneNumber(decoder.decodeString())
            ?: throw NumberFormatException("Phone number is wrong")
    }

    override fun serialize(encoder: Encoder, value: PhoneNumber) {
        encoder.encodeString(value.toString())
    }
}