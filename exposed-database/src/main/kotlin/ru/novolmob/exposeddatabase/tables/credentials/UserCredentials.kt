package ru.novolmob.exposeddatabase.tables.credentials

import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.tables.Users

object UserCredentials: CredentialTable<UserId>() {
    override val parent = reference("user_id", Users, onDelete = ReferenceOption.CASCADE).uniqueIndex()
}