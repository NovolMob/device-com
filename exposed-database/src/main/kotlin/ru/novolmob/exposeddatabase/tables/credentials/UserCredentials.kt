package ru.novolmob.exposeddatabase.tables.credentials

import ru.novolmob.exposeddatabase.tables.Users

object UserCredentials: Credentials() {
    val user = reference("user_id", Users).uniqueIndex()
}