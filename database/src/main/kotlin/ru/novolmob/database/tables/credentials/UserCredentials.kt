package ru.novolmob.database.tables.credentials

import ru.novolmob.database.tables.Users

object UserCredentials: Credentials() {
    val user = reference("user_id", Users)
}