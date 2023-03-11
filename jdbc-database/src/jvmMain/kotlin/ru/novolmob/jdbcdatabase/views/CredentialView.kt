package ru.novolmob.jdbcdatabase.views

import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.tables.Credentials
import ru.novolmob.jdbcdatabase.tables.Users
import ru.novolmob.jdbcdatabase.tables.Workers
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.IParameter
import java.sql.ResultSet

sealed class CredentialView<ID: UUIDable>(
    name: String? = null,
    val first: IParameter<ID>,
    protected val credentials: Credentials<ID>,
): View(name) {

    val id = registerParameter(reference = first)
    val join = registerJoin(first, credentials.ownerId)
    val email = registerParameter(reference = credentials.email)
    val phoneNumber = registerParameter(reference = credentials.phoneNumber)
    val password = registerParameter(reference = credentials.password)

    suspend fun <T> select(id: ID, block: suspend ResultSet.() -> T) = select(expression = this.first eq id, block = block)

    object UserCredentialView: CredentialView<UserId>(
        first = Users.id, credentials = Credentials.UserCredentials
    ) {
        val firstname = registerParameter(reference = Users.firstname)
        val lastname = registerParameter(reference = Users.lastname)
        val patronymic = registerParameter(reference = Users.patronymic)
        val birthday = registerParameter(reference = Users.birthday)
        val cityId = registerParameter(reference = Users.cityId)
        val language = registerParameter(reference = Users.language)
    }

    object WorkerCredentialView: CredentialView<WorkerId>(
        first = Workers.id, credentials = Credentials.WorkerCredentials
    ) {
        val pointId = registerParameter(reference = Workers.pointId)
        val firstname = registerParameter(reference = Workers.firstname)
        val lastname = registerParameter(reference = Workers.lastname)
        val patronymic = registerParameter(reference = Workers.patronymic)
        val language = registerParameter(reference = Workers.language)

        suspend fun <T> select(
            pointId: PointId,
            block: suspend ResultSet.() -> T
        ): T = select(expression = this.pointId eq pointId, block = block)

    }

}