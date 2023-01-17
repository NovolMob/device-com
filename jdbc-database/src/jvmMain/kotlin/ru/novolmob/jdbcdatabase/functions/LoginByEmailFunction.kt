package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

sealed class LoginByEmailFunction<ID>(
    val credentialView: CredentialView<ID>
): RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) where ID : UUIDable, ID: Comparable<ID> {

    val email = email("f_email")
    val password = password("f_password")

    override fun body(): String =
        "DECLARE\n" +
                "    ref refcursor DEFAULT 'cursor_$name';" +
                "BEGIN\n" +
                "    OPEN ref FOR SELECT * FROM $credentialView " +
                "WHERE ${credentialView.email} = $email " +
                "AND ${credentialView.password} = $password LIMIT 1;\n" +
                "    RETURN ref;\n" +
                "END;"

    suspend fun <T> call(
        email: Email,
        password: Password,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.email valueOf email,
            this.password valueOf password,
            block = block
        )

    object UserLoginByEmailFunction: LoginByEmailFunction<UserId>(CredentialView.UserCredentialView)
    object WorkerLoginByEmailFunction: LoginByEmailFunction<WorkerId>(CredentialView.WorkerCredentialView)

}