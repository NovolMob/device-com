package ru.novolmob.jdbcdatabase.utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.jdbcdatabase.databases.Database
import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.functions.*
import ru.novolmob.jdbcdatabase.procedures.*
import ru.novolmob.jdbcdatabase.tables.*
import ru.novolmob.jdbcdatabase.tables.DeviceDetails
import ru.novolmob.jdbcdatabase.views.*

object DatabaseUtil: KoinComponent {
    val tables = listOf(
        DeviceTypes, DeviceTypeDetails,
        Devices, DeviceDetails,
        Cities, CityDetails,
        Points, PointDetails,
        Users, Credentials.UserCredentials,
        Workers, Credentials.WorkerCredentials,
        Baskets, GrantedRights, Orders,
        OrderStatuses, OrderStatusDetails,
        OrderToDeviceTable, OrderToStatusTable,
        DdlCommandLogs
    )

    val views = listOf(
        DetailView.DeviceTypeDetailView, DetailView.DeviceDetailView,
        DetailView.CityDetailView, CredentialView.UserCredentialView,
        CredentialView.WorkerCredentialView, BasketView, DetailView.PointDetailView,
        DetailView.OrderStatusDetailView, OrderView, OrderToStatusView,
        OrderToDeviceView
    )

    val functions = listOf(
        CreationOrUpdateUserFunction,CreationOrUpdateWorkerFunction,
        LoginByEmailFunction.UserLoginByEmailFunction, LoginByEmailFunction.WorkerLoginByEmailFunction, LoginByPhoneNumberFunction.UserLoginByPhoneNumberFunction,
        LoginByPhoneNumberFunction.WorkerLoginByPhoneNumberFunction, GettingBasketFunction,
        GettingCatalogFunction, CreationOrUpdateTableFunction.CreationOrUpdateGranterRightFunction,
        GettingLastStatusFunction, GettingOrderDevicesFunction, ConfirmOrderFunction,
        UpdateTimeFunction(
            listOf(
                DeviceTypes, DeviceTypeDetails,
                Devices, DeviceDetails, CityDetails,
                Points, PointDetails,
                Users, Credentials.UserCredentials,
                Workers, Credentials.WorkerCredentials,
                Baskets, OrderStatuses, OrderStatusDetails
            )
        )
    )

    val procedures = listOf(
        TotalCostProcedure, SetAmountInBasketProcedure,
        RemoveFromBasketProcedure, GetAmountOfPagesProcedure, CancelOrderProcedure
    )

    suspend fun connectAndCreateAllTables(
        url: String,
        user: String = "",
        password: String = ""
    ): Result<Database> = runCatching {
        val database: Database by inject()
        database.connect(url, user, password)
    }.apply {
        onSuccess {
            LoggingTrigger.delete()
            it.statement("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";")
            it.statement("CREATE EXTENSION IF NOT EXISTS \"pgcrypto\";")

            val objects: List<DatabaseObject> =
                tables + views + functions + procedures

            objects.forEach { it.create() }
            LoggingTrigger.create()
        }
    }
}