package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.Email
import ru.novolmob.database.models.Password
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.models.ids.UserId

@Serializable
@Resource("users")
class Users(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val users: Users, val id: UserId)
    @Serializable
    @Resource("login")
    class Login(
        val users: Users,
        val email: Email? = null,
        val phoneNumber: PhoneNumber? = null,
        val password: Password
    )
}