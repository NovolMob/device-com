package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.extensions.TableExtension.email
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.password
import ru.novolmob.jdbcdatabase.extensions.TableExtension.phoneNumber
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

sealed class Credentials<ID: UUIDable>(name: String? = null): IdTable<CredentialId>(name) {

    override val id = idColumn(constructor = ::CredentialId).primaryKey()
    abstract val ownerId: Column<ID>
    val email = email().nullable().unique()
    val phoneNumber = phoneNumber().unique()
    val password = password()
    val updateTime = updateTime()

    suspend fun insert(
        id: CredentialId? = null,
        ownerId: ID,
        email: Email? = null,
        phoneNumber: PhoneNumber,
        password: Password
    ) {
        val list = mutableListOf(
            this.ownerId valueOf ownerId,
            this.email valueOf email,
            this.phoneNumber valueOf phoneNumber,
            this.password valueOf password
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        ownerId: ID,
        email: Email? = null,
        phoneNumber: PhoneNumber,
        password: Password
    ) {
        update(
            newValues = arrayOf(
                this.email valueOf email,
                this.phoneNumber valueOf phoneNumber,
                this.password valueOf password
            ), expression = this.ownerId eq ownerId
        )
    }

    suspend fun update(
        ownerId: ID,
        email: Email? = null,
        phoneNumber: PhoneNumber? = null,
        password: Password? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        email?.let { list.add(this.email valueOf it) }
        phoneNumber?.let { list.add(this.phoneNumber valueOf it) }
        password?.let { list.add(this.password valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.ownerId eq ownerId
        )
    }

    suspend fun delete(
        ownerId: ID
    ) = delete(expression = this.ownerId eq ownerId)

    suspend fun check(
        ownerId: ID? = null, email: Email? = null, phoneNumber: PhoneNumber? = null
    ): Boolean {
        return email?.let { email ->
            phoneNumber?.let { phoneNumber ->
                select(expression = (this.email eq email) or (this.phoneNumber eq phoneNumber)) {
                    if (next()) {
                        get(this@Credentials.ownerId) == ownerId && !next()
                    } else true
                }
            } ?: select(expression = this.email eq email) {
                if (next()) {
                    get(this@Credentials.ownerId) == ownerId && !next()
                } else true
            }
        } ?: phoneNumber?.let { phoneNumber ->
            select(expression = this.phoneNumber eq phoneNumber) {
                if (next()) {
                    get(this@Credentials.ownerId) == ownerId && !next()
                } else true
            }
        } ?: true


//        val emailValid = email?.let {
//            select(expression = this.email eq it) {
//                if (next()) {
//                    get(this@Credentials.ownerId) == ownerId
//                } else true
//            }
//        } ?: true
//        val phoneNumberValid = phoneNumber?.let {
//            select(expression = this.phoneNumber eq it) {
//                if (next()) {
//                    get(this@Credentials.ownerId) == ownerId
//                } else true
//            }
//        } ?: true
//        return emailValid && phoneNumberValid
    }

    object UserCredentials: Credentials<UserId>() {
        override val ownerId = reference("user_id", Users.id).onDeleteCascade().unique()
    }

    object WorkerCredentials: Credentials<WorkerId>() {
         override val ownerId = reference("worker_id", Workers.id).onDeleteCascade().unique()
    }

}