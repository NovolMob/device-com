package ru.novolmob.backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin
import ru.novolmob.backend.routings.AuthorizationRouting.authorizationRouting
import ru.novolmob.backend.routings.BasketRouting.basketRouting
import ru.novolmob.backend.routings.CatalogRouting.catalogRouting
import ru.novolmob.backend.routings.DevicesRouting.deviceRouting
import ru.novolmob.backend.routings.OrderRouting.orderRouting
import ru.novolmob.backend.routings.PointRouting.pointRouting
import ru.novolmob.backend.routings.UserRouting.userRouting
import ru.novolmob.backend.services.DatabaseService
import ru.novolmob.backend.util.AuthUtil.authentication
import ru.novolmob.backend.util.AuthUtil.userPermission
import ru.novolmob.backend.util.NetworkUtil.respondException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.exposedbackendapi.modules.exposedBackendApiModule

suspend fun main() {
    startKoin {
        modules(
            //jdbcDatabaseModule,
            exposedBackendApiModule
        )
    }
    DatabaseService.connectWithExposed()
        .onSuccess {
            embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::backend).start(true)
        }
        .onFailure {
            it.printStackTrace()
        }
}

fun Application.backend() {
    install(ContentNegotiation) {
        json()
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
        authorizationRouting()
        userPermission {
            catalogRouting()
            deviceRouting()
            basketRouting()
            orderRouting()
            pointRouting()
            userRouting()
        }
    }
}