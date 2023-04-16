import react.FC
import react.Props
import react.router.RouteObject
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import react.useState
import services.IProfileService
import utils.useFlow

sealed external interface AppProps: Props {
    var profileService: IProfileService?

    var authorizedRoutes: Array<RouteObject>
    var unauthorizedRoutes: Array<RouteObject>
}

val ReactRouteDomApp = FC<AppProps> {
    val profileService: IProfileService? by useState(it.profileService)
    val isAuthorized by useState(profileService?.profile?.useFlow()?.first != null)
    val routes by useState(if (isAuthorized) it.authorizedRoutes else it.unauthorizedRoutes)

    RouterProvider {
        router = createBrowserRouter(routes)
    }
}

