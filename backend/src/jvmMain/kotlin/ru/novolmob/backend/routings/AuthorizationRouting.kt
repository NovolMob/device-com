package ru.novolmob.backend.routings

import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Login
import ru.novolmob.backend.ktorrouting.user.Logout
import ru.novolmob.backend.ktorrouting.user.Registration
import ru.novolmob.backend.utils.AuthUtil.accessToken
import ru.novolmob.backend.utils.KtorUtil.respond
import ru.novolmob.backendapi.exceptions.badLoginModelException
import ru.novolmob.backendapi.models.LoginModel
import ru.novolmob.backendapi.models.TokenModel
import ru.novolmob.backendapi.repositories.IUserRepository
import ru.novolmob.backendapi.repositories.IWorkerRepository

object AuthorizationRouting: KoinComponent, IRouting {
    private val userRepository: IUserRepository by inject()
    private val workerRepository: IWorkerRepository by inject()

    override fun Route.generalRouting() {
        post<Registration> {
            val either = userRepository.post(createModel = call.receive())
            call.respond(either)
        }

        post<Login> {
            val login: LoginModel = call.receive()
            val either = if (login.email != null) {
                userRepository.login(email = login.email!!, password = login.password)
                    .flatMap {
                        TokenModel(accessToken = it.accessToken()).right()
                    }
            } else if (login.phoneNumber != null) {
                userRepository.login(phoneNumber = login.phoneNumber!!, password = login.password)
                    .flatMap {
                        TokenModel(accessToken = it.accessToken()).right()
                    }
            } else {
                badLoginModelException().left()
            }
            call.respond(either)
        }

        post<Logout> {
            call.respond(Unit.right())
        }

        post<ru.novolmob.backend.ktorrouting.worker.Login> {
            val login: LoginModel = call.receive()
            val either = if (login.email != null) {
                workerRepository.login(email = login.email!!, password = login.password)
                    .flatMap {
                        TokenModel(accessToken = it.accessToken()).right()
                    }
            } else if (login.phoneNumber != null) {
                workerRepository.login(phoneNumber = login.phoneNumber!!, password = login.password)
                    .flatMap {
                        TokenModel(accessToken = it.accessToken()).right()
                    }
            } else {
                badLoginModelException().left()
            }
            call.respond(either)
        }

        post<ru.novolmob.backend.ktorrouting.worker.Logout> {
            call.respond(Unit.right())
        }
    }
}