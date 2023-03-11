package ru.novolmob.exposedbackendapi.services

import org.koin.core.component.KoinComponent
import ru.novolmob.backendapi.utils.DatabaseAuthentication
import org.jetbrains.exposed.sql.Database as ExposedDatabase
import ru.novolmob.exposeddatabase.utils.DatabaseUtil as ExposedDatabaseUtil

object DatabaseService: KoinComponent {

    suspend fun connectWithExposed(): Result<ExposedDatabase> = runCatching {
        val user = DatabaseAuthentication.readUser() ?: throw NullPointerException("Database user is null!")
        val password = DatabaseAuthentication.readPassword(user) ?: throw NullPointerException("Database password is null!")
        connectWithExposed(user, password).getOrThrow()
    }
    suspend fun connectWithExposed(user: String, password: String): Result<ExposedDatabase> =
        ExposedDatabaseUtil.connectAndCreateAllTables(
            url = DatabaseAuthentication.url, user = user,
            password = password
        )
}