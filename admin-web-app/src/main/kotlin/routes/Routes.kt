package routes

import js.core.jso
import pages.LoginPage
import pages.RootPage
import react.create
import react.router.RouteObject
import utils.navigateElement

object Routes {
    const val All = "*"
    const val Root = "/"
    const val Login = "/login"

    val authorizedRoutes = arrayOf<RouteObject>(
        jso {
            path = Root
            element = RootPage.create {

            }
        }
    )

    val unauthorizedRoutes = arrayOf<RouteObject>(
        jso {
            path = Root
            element = navigateElement { to = Login }
        },
        jso {
            path = All
            element = navigateElement { to = Login }
        },
        jso {
            path = Login
            element = LoginPage.create()
        }
    )
}