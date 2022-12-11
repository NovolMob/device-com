package ru.novolmob.database.utils

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManager
import org.jetbrains.exposed.sql.transactions.TransactionManager
import ru.novolmob.database.tables.*
import ru.novolmob.database.tables.credentials.UserCredentials
import ru.novolmob.database.tables.credentials.WorkerCredentials
import java.sql.Connection

object DatabaseUtil {
    fun connectAndCreateAllTables(
        url: String,
        driver: String = org.postgresql.Driver::class.qualifiedName ?: "org.postgres.Driver",
        user: String = "",
        password: String = "",
        setupConnection: (Connection) -> Unit = {},
        databaseConfig: DatabaseConfig? = null,
        manager: (Database) -> TransactionManager = { ThreadLocalTransactionManager(it) }
    ): Result<Database> = runCatching {
        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password,
            setupConnection = setupConnection,
            databaseConfig = databaseConfig,
            manager = manager
        )
    }.apply {
        onSuccess {
            SchemaUtils.createMissingTablesAndColumns(
                UserCredentials, WorkerCredentials,
                Baskets, DeviceDetails, Devices,
                DeviceTypeDetails, DeviceTypes,
                GrantedRights, Orders,
                OrderStatusDetails, OrderStatuses,
                OrderToDeviceTable, PointDetails,
                Points, PointToDeviceTable, Users,
                Workers
            )
        }
    }
}