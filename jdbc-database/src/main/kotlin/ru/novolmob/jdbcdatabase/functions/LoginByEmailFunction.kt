package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

object LoginByEmailFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val email = email("f_email")
    val password = password("f_password")

    override fun body(): String =
        "DECLARE\n" +
                "    ref refcursor DEFAULT 'cursor_$name';" +
                "BEGIN\n" +
                "    OPEN ref FOR SELECT * FROM ${CredentialView.UserCredentialView} " +
                "WHERE ${CredentialView.UserCredentialView.email} = $email " +
                "AND ${CredentialView.UserCredentialView.password} = $password LIMIT 1;\n" +
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

}