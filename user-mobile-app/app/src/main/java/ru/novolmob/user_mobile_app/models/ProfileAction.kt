package ru.novolmob.user_mobile_app.models

sealed class ProfileAction {
    object Logout: ProfileAction()
    object Login: ProfileAction()
}