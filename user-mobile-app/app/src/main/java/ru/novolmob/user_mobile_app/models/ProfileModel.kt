package ru.novolmob.user_mobile_app.models

import ru.novolmob.user_mobile_app.ui.registration.RUSSIANS
import java.time.LocalDate
import java.util.*

class ProfileModel(
    val firstname: String = "Кирилл",
    val lastname: String = "Шиков",
    val patronymic: String = "Дмитриевич",
    val birthday: LocalDate? = LocalDate.now(),
    val city: String? = "Великий Новгород",
    val language: Locale = RUSSIANS,
    val email: String = "example@gmail.com",
    val phoneNumber: String = "+79998765432"
) {
    val initials: String
        get() = "${firstname.first { it.isLetter() }}${lastname.first { it.isLetter() }}"
}