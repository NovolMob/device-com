package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Credentials
import ru.novolmob.jdbcdatabase.tables.Users
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

object CreationOrUpdateUserFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val userId = idColumn(name = "f_user_id", constructor = ::UserId)
    val firstname = firstname("f_firstname")
    val lastname = lastname("f_lastname")
    val patronymic = patronymic("f_patronymic")
    val birthday = birthdate("f_birthdate")
    val cityId = idColumn(name = "f_city_id", constructor = ::CityId)
    val lang = language("f_language")
    val email = email("f_email")
    val phoneNumber = phoneNumber("f_phone_number")
    val password = password("f_password")

    override fun body(): String =
        "DECLARE\n" +
                "    new_user_id uuid;\n" +
                "    ref refcursor DEFAULT 'cursor_$name';\n" +
                "BEGIN\n" +
                "    IF ($userId IS NOT NULL) THEN\n" +
                "        UPDATE $Users SET " +
                "${Users.firstname.name} = $firstname, " +
                "${Users.lastname.name} = $lastname, " +
                "${Users.patronymic.name} = $patronymic, " +
                "${Users.birthday.name} = $birthday, " +
                "${Users.cityId.name} = $cityId, " +
                "${Users.language.name} = $lang " +
                "WHERE ${Users.id} = $userId;\n" +
                "        UPDATE ${Credentials.UserCredentials} SET " +
                "${Credentials.UserCredentials.email.name} = $email, " +
                "${Credentials.UserCredentials.phoneNumber.name} = $phoneNumber, " +
                "${Credentials.UserCredentials.password.name} = $password " +
                "WHERE ${Credentials.UserCredentials.ownerId} = $userId;\n" +
                "        OPEN ref FOR SELECT * FROM ${CredentialView.UserCredentialView} WHERE ${CredentialView.UserCredentialView.id} = $userId;\n" +
                "    ELSE\n" +
                "        INSERT INTO $Users (" +
                "${Users.firstname.name}, " +
                "${Users.lastname.name}, " +
                "${Users.patronymic.name}, " +
                "${Users.birthday.name}, " +
                "${Users.cityId.name}, " +
                Users.language.name +
                ") VALUES ($firstname, $lastname, $patronymic, $birthday, $cityId, $lang) RETURNING ${Users.id} INTO new_user_id;\n" +
                "        INSERT INTO ${Credentials.UserCredentials} (" +
                "${Credentials.UserCredentials.ownerId.name}, " +
                "${Credentials.UserCredentials.email.name}, " +
                "${Credentials.UserCredentials.phoneNumber.name}, " +
                Credentials.UserCredentials.password.name +
                ") VALUES (new_user_id, $email, $phoneNumber, $password);\n" +
                "        OPEN ref FOR SELECT * FROM ${CredentialView.UserCredentialView} WHERE ${CredentialView.UserCredentialView.id} = new_user_id;\n" +
                "    END IF;\n" +
                "    RETURN ref;\n" +
                "END;"

    suspend fun <T> call(
        userId: UserId? = null,
        firstname: Firstname,
        lastname: Lastname,
        patronymic: Patronymic? = null,
        birthday: Birthday? = null,
        cityId: CityId? = null,
        language: Language,
        email: Email? = null,
        phoneNumber: PhoneNumber,
        password: Password,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.userId valueOf userId,
            this.firstname valueOf firstname,
            this.lastname valueOf lastname,
            this.patronymic valueOf patronymic,
            this.birthday valueOf birthday,
            this.cityId valueOf cityId,
            this.lang valueOf language,
            this.email valueOf email,
            this.phoneNumber valueOf phoneNumber,
            this.password valueOf password,
            block = block
        )

}