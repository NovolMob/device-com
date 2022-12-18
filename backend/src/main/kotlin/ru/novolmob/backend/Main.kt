package ru.novolmob.backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.novolmob.exposedbackendapi.mappers.GrantedRightMapper
import ru.novolmob.exposedbackendapi.mappers.ResultRowGrantedRightMapper
import ru.novolmob.exposedbackendapi.repositories.GrantedRightRepositoryImpl
import ru.novolmob.backend.services.DatabaseService
import ru.novolmob.backend.util.AuthUtil.workerPermission
import ru.novolmob.backend.util.NetworkUtil.respondException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.repositories.IGrantedRightRepository

suspend fun main() {
    DatabaseService.connect()
        .onSuccess {
            embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::backend).start(true)
        }
        .onFailure {
            it.printStackTrace()
        }
}

fun Application.backend() {
    startKoin {
        modules(
            module {
                singleOf(::GrantedRightMapper)
                singleOf(::ResultRowGrantedRightMapper)
                single<IGrantedRightRepository> {
                    GrantedRightRepositoryImpl(get<GrantedRightMapper>(), get<ResultRowGrantedRightMapper>())
                }
            }
        )
    }
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception { call: ApplicationCall, cause: Exception ->
            call.respondException(
                code = BackendExceptionCode.UNKNOWN,
                message = cause.message ?: "Unknown error"
            )
        }
    }
//    authentication {
//        jwt {
//            val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
//            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
//            verifier(
//                JWT
//                    .require(Algorithm.HMAC256("secret"))
//                    .withAudience(jwtAudience)
//                    .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
//                    .build()
//            )
//            validate { credential ->
//                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//            }
//        }
//    }
    install(Resources)
    routing {
        workerPermission {
            get {
                call.respond("Hello world")
            }
        }
    }
}