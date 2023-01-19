package ru.novolmob.jdbcbackendapi.services

import org.koin.core.component.KoinComponent
import ru.novolmob.backendapi.utils.DatabaseAuthentication
import ru.novolmob.jdbcdatabase.databases.Database
import ru.novolmob.jdbcdatabase.utils.DatabaseUtil

object DatabaseService: KoinComponent {

    suspend fun connectWithJdbcTest() = DatabaseUtil.connectAndCreateAllTables(
        "jdbc:postgresql://192.168.31.227:5432/postgres", "root", "root"
    )

    suspend fun connectWithJdbc(): Result<Database> = runCatching {
        val user = DatabaseAuthentication.readUser() ?: throw NullPointerException("Database user is null!")
        val password = DatabaseAuthentication.readPassword(user) ?: throw NullPointerException("Database password is null!")
        connectWithJdbc(user, password).getOrThrow()
    }
    suspend fun connectWithJdbc(user: String, password: String): Result<Database> =
        DatabaseUtil.connectAndCreateAllTables(DatabaseAuthentication.url, user, password)
}