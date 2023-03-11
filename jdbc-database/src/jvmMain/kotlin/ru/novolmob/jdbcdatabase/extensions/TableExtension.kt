package ru.novolmob.jdbcdatabase.extensions

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.UUID
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.tables.parameters.types.*

object TableExtension {

    fun <T: UUIDable> Table.idColumn(name: String = "id", constructor: (UUID) -> T) = registerColumn(name, CustomUuidParameterType(constructor))
        .default(DatabaseVocabulary.UUID_GENERATION).unique()
    fun Table.firstname(name: String = "firstname") = registerColumn(name, SerializableParameterType(Firstname.serializer()))
    fun Table.lastname(name: String = "lastname") = registerColumn(name, SerializableParameterType(Lastname.serializer()))
    fun Table.patronymic(name: String = "patronymic") = registerColumn(name, SerializableParameterType(Patronymic.serializer()))
    fun Table.birthdate(name: String = "birthdate") = registerColumn(name, CustomDateParameterType(::Birthday))
    fun Table.language(name: String = "language") = registerColumn(name, SerializableParameterType(Language.serializer()))
    fun Table.updateTime(name: String = "update_time") = registerColumn(name, CustomDateTimeParameterType(::UpdateTime))
        .default(DatabaseVocabulary.UPDATE_TIME_GENERATION)
    fun Table.creationTime(name: String = "creation_time") = registerColumn(name, CustomDateTimeParameterType(::CreationTime))
        .default(DatabaseVocabulary.CREATION_TIME_GENERATION)

    fun Table.title(name: String = "title") = registerColumn(name, SerializableParameterType(Title.serializer()))
    fun Table.description(name: String = "description") = registerColumn(name, SerializableParameterType(Description.serializer()))
    fun Table.amount(name: String = "amount") = registerColumn(name, CustomIntegerParameterType(::Amount))
    fun Table.code(name: String = "code") = registerColumn(name, SerializableParameterType(Code.serializer()))
    fun Table.price(name: String = "price", precision: Int, scale: Int) = registerColumn(name, CustomDecimalParameterType(::Price, precision, scale))
    fun Table.features(name: String = "features") = registerColumn(name, SerializableParameterType(Features.serializer()))
    fun Table.email(name: String = "email") = registerColumn(name, SerializableParameterType(Email.serializer()))
    fun Table.phoneNumber(name: String = "phone_number") = registerColumn(name, SerializableParameterType(
        PhoneNumber.serializer())
    )
    fun Table.password(name: String = "password") = registerColumn(name, SerializableParameterType(Password.serializer()))
    fun Table.address(name: String = "address") = registerColumn(name, SerializableParameterType(Address.serializer()))
    fun Table.schedule(name: String = "schedule") = registerColumn(name, SerializableParameterType(Schedule.serializer()))

}