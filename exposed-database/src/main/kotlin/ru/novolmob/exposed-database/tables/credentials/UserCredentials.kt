package ru.novolmob.`exposed-database`.tables.credentials

import ru.novolmob.`exposed-database`.tables.Users

object UserCredentials: Credentials() {
    val user = reference("user_id", Users).uniqueIndex()
}