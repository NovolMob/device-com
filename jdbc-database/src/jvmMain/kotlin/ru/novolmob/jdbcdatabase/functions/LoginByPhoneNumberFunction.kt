package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

sealed class LoginByPhoneNumberFunction<ID>(
    val credentialView: CredentialView<ID>
): RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) where ID : UUIDable, ID: Comparable<ID> {

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

    object UserLoginByPhoneNumberFunction: LoginByPhoneNumberFunction<UserId>(CredentialView.UserCredentialView)
    object WorkerLoginByPhoneNumberFunction: LoginByPhoneNumberFunction<WorkerId>(CredentialView.WorkerCredentialView)

}