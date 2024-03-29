package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Catalog
import ru.novolmob.backend.utils.AuthUtil.user
import ru.novolmob.backend.utils.KtorUtil.respond
import ru.novolmob.backendapi.repositories.ICatalogRepository

object CatalogRouting: KoinComponent, IRouting {
    private val catalogRepository: ICatalogRepository by inject()

    override fun Route.routingForUser() {
        get<Catalog> {
            val user = user()
            val either = catalogRepository.getCatalog(sample = it, language = user.language)
            call.respond(either = either)
        }
    }

}