package ru.novolmob.core.utils

import ru.novolmob.core.models.PhoneNumber

object PhoneNumberUtil {
    val nonNumberRegex = Regex("[^0-9]")
    const val PHONE_NUMBER_PATTERN = "+%s (%s) %s"

    fun stringClientNumber(number: ULong): String {
        return Regex("\\d{1,3}")
            .findAll(number.toString())
            .joinToString("-") { it.value }
    }

    fun stringPhoneNumber(countryCode: ULong, innerCode: ULong, clientNumber: ULong): String =
        PHONE_NUMBER_PATTERN.format(
            countryCode.toString(),
            innerCode.toString(),
            stringClientNumber(clientNumber)
        )

    fun deserializePhoneNumber(string: String): PhoneNumber? {
        val split = string
            .split(' ', limit = 3)
            .map { nonNumberRegex.replace(it, "") }
            .takeIf { it.all(String::isNotEmpty) } ?: return null
        return PhoneNumber(
            countryCode = split[0].toULong(),
            innerCode = split[1].toULong(),
            clientNumber = split[2].toULong()
        )
    }

}