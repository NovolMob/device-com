package ru.novolmob.database.extensions

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import ru.novolmob.database.columntypes.*
import ru.novolmob.database.models.*
import ru.novolmob.database.models.ids.UUIDable
import java.util.*

object TableExtension {
    fun Table.phoneNumber(name: String = "phone_number", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<PhoneNumber> =
        registerColumn(
            name,
            CustomTextColumnType(
                PhoneNumber::class,
                PhoneNumber.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.address(name: String = "address", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Address> =
        registerColumn(
            name,
            CustomTextColumnType(
                Address::class,
                Address.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.code(name: String = "code", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Code> =
        registerColumn(
            name,
            CustomTextColumnType(
                Code::class,
                Code.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.email(name: String = "email", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Email> =
        registerColumn(
            name,
            CustomTextColumnType(
                Email::class,
                Email.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.password(name: String = "password", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Password> =
        registerColumn(
            name,
            CustomTextColumnType(
                Password::class,
                Password.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.amount(name: String = "amount"): Column<Amount> =
        registerColumn(
            name,
            CustomIntegerColumnType(::Amount)
        )

    fun Table.birthday(name: String = "birthday"): Column<Birthday> =
        registerColumn(
            name,
            CustomDateColumnType(::Birthday)
        )

    fun Table.city(name: String = "city", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<City> =
        registerColumn(
            name,
            CustomTextColumnType(
                City::class,
                City.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.creationDate(name: String = "creation_date"): Column<CreationDate> =
        registerColumn(
            name,
            CustomDateColumnType(::CreationDate)
        )

    fun Table.description(name: String = "description", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Description> =
        registerColumn(
            name,
            CustomTextColumnType(
                Description::class,
                Description.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.features(name: String = "features", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Features> =
        registerColumn(
            name,
            CustomTextColumnType(
                Features::class,
                Features.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.firstname(name: String = "firstname", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Firstname> =
        registerColumn(
            name,
            CustomTextColumnType(
                Firstname::class,
                Firstname.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.language(name: String = "language", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Language> =
        registerColumn(
            name,
            CustomTextColumnType(
                Language::class,
                Language.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.lastname(name: String = "lastname", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Lastname> =
        registerColumn(
            name,
            CustomTextColumnType(
                Lastname::class,
                Lastname.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.patronymic(name: String = "patronymic", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Patronymic> =
        registerColumn(
            name,
            CustomTextColumnType(
                Patronymic::class,
                Patronymic.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.price(name: String = "price", precision: Int, scale: Int): Column<Price> =
        registerColumn(
            name,
            CustomDecimalColumnType(::Price, precision, scale)
        )

    fun Table.schedule(name: String = "schedule", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Schedule> =
        registerColumn(
            name,
            CustomTextColumnType(
                Schedule::class,
                Schedule.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.title(name: String = "title", json: Json = Json, collate: String? = null, eagerLoading: Boolean = false): Column<Title> =
        registerColumn(
            name,
            CustomTextColumnType(
                Schedule::class,
                Schedule.serializer(),
                json, collate, eagerLoading
            )
        )

    fun Table.updateDate(name: String = "update_date"): Column<UpdateDate> =
        registerColumn(
            name,
            CustomDateColumnType(::UpdateDate)
        )

    fun <T: UUIDable> Table.idColumn(name: String = "id", constructor: (UUID) -> T): Column<T> =
        registerColumn<T>(name, CustomUUIDColumnType(constructor)).clientDefault { constructor(UUID.randomUUID()) }
}