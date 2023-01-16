package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.PhoneNumberSerializer
import ru.novolmob.core.utils.IStringChecking
import ru.novolmob.core.utils.PhoneNumberUtil

@Serializable(with = PhoneNumberSerializer::class)
data class PhoneNumber(
    val countryCode: ULong,
    val innerCode: ULong,
    val clientNumber: ULong
) {
    override fun toString(): String =
        PhoneNumberUtil.stringPhoneNumber(countryCode, innerCode, clientNumber)

    companion object {
        fun String.phoneNumber(): PhoneNumber? = PhoneNumberUtil.deserializePhoneNumber(this)
    }
}

expect object PhoneNumberChecking: IStringChecking
