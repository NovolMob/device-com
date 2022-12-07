package ru.novolmob.database.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.utils.PhoneNumberUtil

@Serializable
data class PhoneNumber(
    val countryCode: ULong,
    val innerCode: ULong,
    val clientNumber: ULong
) {
    override fun toString(): String =
        PhoneNumberUtil.stringPhoneNumber(countryCode, innerCode, clientNumber)
}
