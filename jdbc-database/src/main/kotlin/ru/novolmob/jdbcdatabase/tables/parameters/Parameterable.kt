package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.jdbcdatabase.tables.parameters.types.*
import java.util.*

interface Parameterable {
    fun <T: UUIDable> idColumn(name: String = "id", constructor: (UUID) -> T) = registerParameter(name, CustomUuidParameterType(constructor))
    fun firstname(name: String = "firstname") = registerParameter(name, SerializableParameterType(Firstname.serializer()))
    fun lastname(name: String = "lastname") = registerParameter(name, SerializableParameterType(Lastname.serializer()))
    fun patronymic(name: String = "patronymic") = registerParameter(name, SerializableParameterType(Patronymic.serializer()))
    fun birthdate(name: String = "birthdate") = registerParameter(name, CustomDateParameterType(::Birthday))
    fun language(name: String = "language") = registerParameter(name, SerializableParameterType(Language.serializer()))
    fun updateTime(name: String = "update_time") = registerParameter(name, CustomDateTimeParameterType(::UpdateTime))
    fun creationTime(name: String = "creation_time") = registerParameter(name, CustomDateTimeParameterType(::CreationTime))
    fun title(name: String = "title") = registerParameter(name, SerializableParameterType(Title.serializer()))
    fun description(name: String = "description") = registerParameter(name, SerializableParameterType(
        Description.serializer())
    )
    fun amount(name: String = "amount") = registerParameter(name, CustomIntegerParameterType(::Amount))
    fun code(name: String = "code") = registerParameter(name, SerializableParameterType(Code.serializer()))
    fun price(name: String = "price", precision: Int, scale: Int) = registerParameter(name, CustomDecimalParameterType(::Price, precision, scale)
    )
    fun features(name: String = "features") = registerParameter(name, SerializableParameterType(Features.serializer()))
    fun email(name: String = "email") = registerParameter(name, SerializableParameterType(Email.serializer()))
    fun phoneNumber(name: String = "phone_number") = registerParameter(name, SerializableParameterType(
        PhoneNumber.serializer())
    )
    fun password(name: String = "password") = registerParameter(name, SerializableParameterType(Password.serializer()))
    fun text(name: String) = registerParameter(name, PrimitiveParameterType.Text)
    fun decimal(name: String, precision: Int, scale: Int) =
        registerParameter(name, PrimitiveParameterType.Decimal(precision, scale))
    fun varchar(name: String, n: Int) = registerParameter(name, PrimitiveParameterType.VarChar(n))
    fun uuid(name: String) = registerParameter(name, PrimitiveParameterType.Uuid)
    fun integer(name: String) = registerParameter(name, PrimitiveParameterType.Integer)
    fun date(name: String) = registerParameter(name, PrimitiveParameterType.Date)
    fun address(name: String = "address") = registerParameter(name, SerializableParameterType(Address.serializer()))
    fun schedule(name: String = "schedule") = registerParameter(name, SerializableParameterType(Schedule.serializer()))

    fun <T: Any> registerParameter(name: String, type: IParameterType<T>): IParameter<T>
}