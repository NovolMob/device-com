package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

object LoginByPhoneNumberFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val phoneNumber = phoneNumber("f_phone_number")
    val password = password("f_password")

    override fun body(): String =
        "DECLARE\n" +
                "    ref refcursor DEFAULT 'cursor_$name';" +
                "BEGIN\n" +
                "    OPEN ref FOR SELECT * FROM ${CredentialView.UserCredentialView} " +
                "WHERE ${CredentialView.UserCredentialView.phoneNumber} = $phoneNumber " +
                "AND ${CredentialView.UserCredentialView.password} = $password LIMIT 1;\n" +
                "    RETURN ref;\n" +
                "END;"

    suspend fun <T> call(
        phoneNumber: PhoneNumber,
        password: Password,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.phoneNumber valueOf phoneNumber,
            this.password valueOf password,
            block = block
        )

}