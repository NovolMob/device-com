package ru.novolmob.user_mobile_app.models

import java.time.LocalDate
import java.util.*

data class ProfileUpdateModel(
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val birthday: LocalDate? = null,
    val city: String? = null,
    val language: Locale? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null
)