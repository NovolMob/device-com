package ru.novolmob.jdbcdatabase.extensions

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.tables.columns.types.*
import java.util.UUID

object TableExtension {

    fun <T: UUIDable> Table.idColumn(name: String = "id", constructor: (UUID) -> T) = registerColumn(name, CustomUuidColumnType(constructor))
        .default(DatabaseVocabulary.UUID_GENERATION).unique().primaryKey()
    fun Table.firstname(name: String = "firstname") = registerColumn(name, SerializableColumnType(Firstname.serializer()))
    fun Table.lastname(name: String = "lastname") = registerColumn(name, SerializableColumnType(Lastname.serializer()))
    fun Table.patronymic(name: String = "patronymic") = registerColumn(name, SerializableColumnType(Patronymic.serializer()))
    fun Table.birthdate(name: String = "birthdate") = registerColumn(name, CustomDateColumnType(::Birthday))
    fun Table.city(name: String = "city") = registerColumn(name, SerializableColumnType(City.serializer()))
    fun Table.language(name: String = "language") = registerColumn(name, SerializableColumnType(Language.serializer()))
    fun Table.updateTime(name: String = "update_time") = registerColumn(name, CustomDateTimeColumnType(::UpdateTime))
        .default(DatabaseVocabulary.UPDATE_TIME_GENERATION)
    fun Table.creationTime(name: String = "creation_time") = registerColumn(name, CustomDateTimeColumnType(::CreationTime))
        .default(DatabaseVocabulary.CREATION_TIME_GENERATION)

    fun Table.title(name: String = "title") = registerColumn(name, SerializableColumnType(Title.serializer()))
    fun Table.description(name: String = "description") = registerColumn(name, SerializableColumnType(Description.serializer()))
    fun Table.amount(name: String = "amount") = registerColumn(name, CustomIntegerColumnType(::Amount))
    fun Table.code(name: String = "code") = registerColumn(name, SerializableColumnType(Code.serializer()))
    fun Table.price(name: String = "price", precision: Int, scale: Int) = registerColumn(name, CustomDecimalColumnType(::Price, precision, scale))
    fun Table.features(name: String = "features") = registerColumn(name, SerializableColumnType(Features.serializer()))


}