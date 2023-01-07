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

            /*newSuspendedTransaction {

                Device.new {
                    article = Code("AAA-123")
                    type = DeviceType.new {}.also {
                        DeviceTypeDetail.new {
                            deviceType = it
                            title = Title("Первый тип устройств")
                            description = Description("Описание типа устройства")
                            language = Language("ru")
                        }
                    }
                    price = Price(1999.0.toBigDecimal())
                }.also {
                    DeviceDetail.new {
                        device = it
                        title = Title("Первое устройство")
                        description = Description("Описание устройства")
                        features = Features(
                            mapOf(
                                "Надёжность" to "Хорошая",
                                "Надёжность" to "Хорошая",
                                "Надёжность" to "Хорошая"
                            )
                        )
                        language = Language("ru")
                    }
                }
            }*/

            /*newSuspendedTransaction {
                PointDetail.new {
                    point = Point.new {
                        city = City("Великий Новгород")
                    }
                    address = Address("г.Великий Новгород ул.Ломоносова д.11")
                    schedule = Schedule(
                        mapOf(
                            "Пн-Пт" to "10:00 - 21:00",
                            "Сб-Вс" to "10:00 - 18:00",
                        )
                    )
                    description = Description("Описание пункта")
                    language = Language("ru")
                }
            }*/

            /*newSuspendedTransaction {
                PointToDeviceEntity.new {
                    point = Point.findById(PointId(UUID.fromString("27881e78-154e-45c5-8f35-69aaf76cee1a")))!!
                    device = Device.findById(DeviceId(UUID.fromString("4e77c4a9-a0df-420a-b1ed-00c608f01e42")))!!
                    amount = Amount(6)
                }
            }*/

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