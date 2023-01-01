package ru.novolmob.backend.services

import org.jetbrains.exposed.sql.Database as ExposedDatabase
import ru.novolmob.jdbcdatabase.databases.Database as JdbcDatabase
import org.koin.core.component.KoinComponent
import ru.novolmob.exposeddatabase.utils.DatabaseUtil as ExposedDatabaseUtil
import ru.novolmob.jdbcdatabase.utils.DatabaseUtil as JdbcDatabaseUtil

object DatabaseService: KoinComponent {
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

    suspend fun connectWithExposed(): Result<ExposedDatabase> = runCatching {
        val user = readUser() ?: throw NullPointerException("Database user is null!")
        val password = readPassword(user) ?: throw NullPointerException("Database password is null!")
        connectWithExposed(user, password).getOrThrow()
    }
    suspend fun connectWithExposed(user: String, password: String): Result<ExposedDatabase> =
        ExposedDatabaseUtil.connectAndCreateAllTables(
            url = URL, user = user,
            password = password
        )

    fun connectWithJdbc(): Result<JdbcDatabase> = runCatching {
        val user = readUser() ?: throw NullPointerException("Database user is null!")
        val password = readPassword(user) ?: throw NullPointerException("Database password is null!")
        connectWithJdbc(user, password).getOrThrow()
    }
    fun connectWithJdbc(user: String, password: String): Result<JdbcDatabase> =
        JdbcDatabaseUtil.connectAndCreateAllTables(URL, user, password)

}