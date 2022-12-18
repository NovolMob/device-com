package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.serializers.PhoneNumberSerializer

@Serializable(with = PhoneNumberSerializer::class)
data class PhoneNumber(
    val countryCode: ULong,
    val innerCode: ULong,
    val clientNumber: ULong
) {
    override fun toString(): String =
        PhoneNumberUtil.stringPhoneNumber(countryCode, innerCode, clientNumber)
}
