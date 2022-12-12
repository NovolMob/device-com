package ru.novolmob.backend.services

import org.jetbrains.exposed.sql.Database
import ru.novolmob.database.utils.DatabaseUtil

object DatabaseService {
    private const val URL = "jdbc:postgresql://db.nmmdvscyjuohfkwkmefh.supabase.co:5432/postgres"
    private const val DATABASE_USER_PROPERTY = "DATABASE_USER"
    private const val DATABASE_PASSWORD_PROPERTY = "DATABASE_PASSWORD"

    private fun readUser(): String? =
        System.getenv(DATABASE_USER_PROPERTY) ?: let {
                print("Enter database username: ")
                readln()
            }.takeIf { it.isNotEmpty() }
    private fun readPassword(user: String): String? =
        System.getenv(DATABASE_PASSWORD_PROPERTY) ?: let {
                print("Enter password for $user: ")
                readln()
            }.takeIf { it.isNotEmpty() }

    suspend fun connect(): Result<Database> = runCatching {
        val user = readUser() ?: throw NullPointerException("Database user is null!")
        val password = readPassword(user) ?: throw NullPointerException("Database password is null!")
        connect(user, password).getOrThrow()
    }
    suspend fun connect(user: String, password: String): Result<Database> =
        DatabaseUtil.connectAndCreateAllTables(
            url = URL, user = user,
            password = password
        )

}