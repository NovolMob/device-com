package ru.novolmob.core.utils

import ru.novolmob.core.models.PhoneNumber

object PhoneNumberUtil {
    val nonNumberRegex = Regex("[^0-9]")

    fun stringClientNumber(number: ULong): String {
        return Regex("\\d{1,3}")
            .findAll(number.toString())
            .joinToString("-") { it.value }
    }

    fun stringPhoneNumber(countryCode: ULong, innerCode: ULong, clientNumber: ULong): String =
        "+${countryCode} ($innerCode) ${stringClientNumber(clientNumber)}"

    fun deserializePhoneNumber(string: String): PhoneNumber? {
        val split = string.replace(Regex("\\D"), "")
        return PhoneNumber(
            countryCode = split.substring(0..0).toULong(),
            innerCode = split.substring(1..3).toULong(),
            clientNumber = split.substring(startIndex = 4).toULong()
        )
    }
}
