package ru.novolmob.backend

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
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

val HOST = System.getenv("HOST") ?: "192.168.31.227"
val PORT = System.getenv("PORT")?.toInt() ?: 8080

suspend fun main() {
    startKoin {
        modules(
            jdbcBackendApiModule,
//            exposedBackendApiModule
        )
    }
    DatabaseService.connectWithJdbc()
        .onSuccess {
            embeddedServer(Netty, host = HOST, port = PORT, module = Application::backend).start(true)
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
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
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
            UserRouting,
            WorkerRouting
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