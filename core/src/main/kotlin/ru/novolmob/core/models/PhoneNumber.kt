package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.PhoneNumberSerializer
import ru.novolmob.core.utils.PhoneNumberUtil

@Serializable(with = PhoneNumberSerializer::class)
data class PhoneNumber(
    val countryCode: ULong,
    val innerCode: ULong,
    val clientNumber: ULong
) {
    override fun toString(): String =
        PhoneNumberUtil.stringPhoneNumber(countryCode, innerCode, clientNumber)
}
