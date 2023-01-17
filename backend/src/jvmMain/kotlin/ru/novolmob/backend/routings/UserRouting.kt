package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.User
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.repositories.IUserRepository

object UserRouting: KoinComponent, IRouting {
    private val userRepository: IUserRepository by inject()

    override fun Route.routingForUser() {
        get<User> {
            val user = user()
            val either = userRepository.get(id = user.id)
            call.respond(either)
        }
        post<User> {
            val user = user()
            val either = userRepository.post(id = user.id, createModel = call.receive())
            either.fold(
                ifLeft = {},
                ifRight = {
                    user(user = it)
                }
            )
            call.respond(either)
        }
        put<User> {
            val user = user()
            val either = userRepository.put(id = user.id, updateModel = call.receive())
            either.fold(
                ifLeft = {},
                ifRight = {
                    user(user = it)
                }
            )
            call.respond(either)
        }
    }
}