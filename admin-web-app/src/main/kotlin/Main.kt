import modules.clientModule
import modules.repositoryModule
import modules.serviceModule
import modules.storageModule
import org.koin.core.context.startKoin
import react.create
import react.dom.client.createRoot
import routes.Routes
import utils.ServiceUtil.getKoin
import web.dom.document

fun main() {
    koin()

    val root = document.getElementById("root")!!
    createRoot(root).render(
        ReactRouteDomApp.create {
            profileService = getKoin().get()

            authorizedRoutes = Routes.authorizedRoutes
            unauthorizedRoutes = Routes.unauthorizedRoutes
        }
    )
}

fun koin() {
    startKoin {
        modules(
            clientModule,
            repositoryModule,
            serviceModule,
            storageModule
        )
    }
}