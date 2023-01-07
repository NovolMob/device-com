package ru.novolmob.jdbcdatabase.utils

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.jdbcdatabase.databases.Database
import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.tables.*
import ru.novolmob.jdbcdatabase.tables.expressions.DeviceDetails
import ru.novolmob.jdbcdatabase.views.DetailView

object DatabaseUtil: KoinComponent {
    val tables = listOf(
        DeviceTypes, DeviceTypeDetails,
        Devices, DeviceDetails,
        Cities, CityDetails,
        Users, Baskets
    )

    val views = listOf(
        DetailView.DeviceTypeDetailView, DetailView.DeviceDetailView, DetailView.CityDetailView
    )

    fun connectAndCreateAllTables(
        url: String,
        user: String = "",
        password: String = ""
    ): Result<Database> = runCatching {
        val database: Database by inject()
        database.connect(url, user, password)
    }.apply {
        onSuccess {
            val objects: List<DatabaseObject> =
                tables + views

            objects.forEach(DatabaseObject::create)
        }
    }
}