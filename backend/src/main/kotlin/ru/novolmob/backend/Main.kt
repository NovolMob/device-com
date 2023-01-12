package ru.novolmob.backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import ru.novolmob.backend.routings.*
import ru.novolmob.backend.services.DatabaseService
import ru.novolmob.backend.util.AuthUtil.authentication
import ru.novolmob.backend.util.AuthUtil.userPermission
import ru.novolmob.backend.util.KtorUtil.respondException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.jdbcbackendapi.modules.jdbcBackendApiModule

suspend fun main() {
    startKoin {
        modules(
            jdbcBackendApiModule,
//            exposedBackendApiModule
        )
    }
    DatabaseService.connectWithJdbc()
        .onSuccess {
            embeddedServer(Netty, host = "192.168.31.196", port = 8080, module = Application::backend).start(true)
        }
        .onFailure {
            it.printStackTrace()
        }
}

fun Application.backend() {
    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = false
            }
        )
    }
    install(StatusPages) {
        exception { call: ApplicationCall, cause: Exception ->
            call.respondException(
                code = BackendExceptionCode.UNKNOWN,
                message = cause.message ?: "Unknown error"
            )
            cause.printStackTrace()
        }
    }
    authentication()

    install(Resources)
    routing {
        apply(
            AuthorizationRouting,
            BasketRouting,
            CatalogRouting,
            CityRouting,
            DevicesRouting,
            OrderRouting,
            PointRouting,
            UserRouting
        )
    }
}

fun Route.apply(vararg routing: IRouting) {
    routing.forEach {
        with(it) {
            generalRouting()
        }
    }
    authenticate {
        userPermission {
            routing.forEach {
                with(it) {
                    routingForUser()
                }
            }
        }
        routing.forEach {
            with(it) {
                routingForWorker()
            }
        }
    }
}