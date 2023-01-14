package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.serializers.PhoneNumberSerializer
import ru.novolmob.core.utils.PhoneNumberUtil
import ru.novolmob.core.utils.StringChecking

@Serializable(with = PhoneNumberSerializer::class)
data class PhoneNumber(
    val countryCode: ULong,
    val innerCode: ULong,
    val clientNumber: ULong
) {
    override fun toString(): String =
        PhoneNumberUtil.stringPhoneNumber(countryCode, innerCode, clientNumber)

    companion object: StringChecking() {
        override val regex = Regex("\\d{11}")

        fun String.phoneNumber(): PhoneNumber? = PhoneNumberUtil.deserializePhoneNumber(this)
    }
}
