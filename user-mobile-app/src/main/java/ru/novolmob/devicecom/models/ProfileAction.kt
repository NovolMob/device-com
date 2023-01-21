package ru.novolmob.devicecom.models

sealed class ProfileAction {
    object Unauthorized: ProfileAction()
    object Logout: ProfileAction()
    object Login: ProfileAction()
    object Registered: ProfileAction()
}