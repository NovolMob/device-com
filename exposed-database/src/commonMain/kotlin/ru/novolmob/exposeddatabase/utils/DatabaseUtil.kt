package ru.novolmob.exposeddatabase.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManager
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.exposeddatabase.tables.*
import ru.novolmob.exposeddatabase.tables.credentials.UserCredentials
import ru.novolmob.exposeddatabase.tables.credentials.WorkerCredentials
import ru.novolmob.exposeddatabase.tables.details.*
import java.sql.Connection

object DatabaseUtil {
    suspend fun connectAndCreateAllTables(
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
            newSuspendedTransaction(Dispatchers.IO) {
                SchemaUtils.createMissingTablesAndColumns(
                    UserCredentials, WorkerCredentials,
                    Cities, CityDetails,
                    Baskets, DeviceDetails, Devices,
                    DeviceTypeDetails, DeviceTypes,
                    GrantedRights, Orders,
                    OrderStatusDetails, OrderStatuses,
                    OrderToStatusTable, OrderToDeviceTable,
                    PointDetails, Points,
                    Users, Workers
                )
            }
        }
    }
}