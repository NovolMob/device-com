package ru.novolmob.user_mobile_app.models

sealed class ProfileAction {
    object Unauthorized: ProfileAction()
    object Logout: ProfileAction()
    object Login: ProfileAction()
    object Registered: ProfileAction()
}