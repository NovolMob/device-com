package ru.novolmob.user_mobile_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.novolmob.user_mobile_app.R
import ru.novolmob.user_mobile_app.models.NavigationTab
import ru.novolmob.user_mobile_app.ui.basket.BasketScreen
import ru.novolmob.user_mobile_app.ui.catalog.CatalogScreen
import ru.novolmob.user_mobile_app.ui.profile.ProfileScreen

sealed class NavigationRoute(val route: String) {
    object Authorization: NavigationRoute("Authorization") {
        fun NavHostController.navigateToAuthorization() = navigate(route = route)
        fun NavDestination.isAuthorization(): Boolean = this.route == route
    }

    object Registration: NavigationRoute("Registration") {
        fun NavHostController.navigateToRegistration() = navigate(route = route)
        fun NavDestination.isRegistration(): Boolean = this.route == route
    }

    sealed class Main(route: String): NavigationRoute("${Companion.route}/$route") {
        protected val badge = MutableStateFlow(0)
        abstract val navigationTab: NavigationTab

        suspend fun badge(value: Int) = badge.emit(value)

        companion object: NavigationRoute("Main") {
            private val regex = Regex("$route\\/*")
            val tabs = listOf(Profile, Catalog, Basket)

            fun NavHostController.navigateToMain() = navigate(route = route)
            fun NavDestination.isMain(): Boolean = regex.matches(this.route ?: "")
        }

        object Catalog: Main("Catalog") {
            override val navigationTab: NavigationTab = NavigationTab(
                route = route,
                displayName = R.string.catalog,
                imageVector = materialIcon(name = route) {
                    materialPath {
                        moveTo(4.0f, 24.0f)
                        horizontalLineToRelative(16.0f)
                        verticalLineToRelative(-2.0f)
                        lineTo(4.0f, 22.0f)
                        verticalLineToRelative(2.0f)
                        close()

                        moveTo(4.0f, 4.0f)
                        horizontalLineToRelative(16.0f)
                        verticalLineToRelative(-2.0f)
                        lineTo(4.0f, 2.0f)
                        verticalLineToRelative(2.0f)
                        close()

                        moveTo(2.0f, 2.0f)
                        verticalLineToRelative(22.0f)
                        horizontalLineToRelative(2.0f)
                        lineTo(4.0f, 2.0f)
                        horizontalLineToRelative(-2.0f)
                        close()

                        moveTo(20.0f, 2.0f)
                        verticalLineToRelative(22.0f)
                        horizontalLineToRelative(2.0f)
                        lineTo(22.0f, 2.0f)
                        horizontalLineToRelative(-2.0f)
                        close()



                        moveTo(6.0f, 18.0f)
                        horizontalLineToRelative(12.0f)
                        verticalLineToRelative(-2.0f)
                        lineTo(6.0f, 16.0f)
                        verticalLineToRelative(2.0f)
                        close()

                        moveTo(6.0f, 14.0f)
                        horizontalLineToRelative(12.0f)
                        verticalLineToRelative(-2.0f)
                        lineTo(6.0f, 12.0f)
                        verticalLineToRelative(2.0f)
                        close()

                        moveTo(6.0f, 10.0f)
                        horizontalLineToRelative(12.0f)
                        verticalLineToRelative(-2.0f)
                        lineTo(6.0f, 8.0f)
                        verticalLineToRelative(2.0f)
                        close()
                    }
                },
                badgeFlow = badge.asStateFlow(),
                navigate = { navigateToCatalog() }
            ) { modifier, navHostController, _ ->
                CatalogScreen(
                    modifier = modifier,
                    navHostController = navHostController
                )
            }

            fun NavHostController.navigateToCatalog() = navigate(route = route)
            fun NavDestination.isCatalog(): Boolean = this.route == route
        }

        object Basket: Main("Basket") {
            override val navigationTab: NavigationTab = NavigationTab(
                route = route,
                displayName = R.string.basket,
                imageVector = Icons.Rounded.ShoppingCart,
                badgeFlow = badge.asStateFlow(),
                navigate = { navigateToBasket() }
            ) { modifier, navHostController, _ ->
                BasketScreen(
                    modifier = modifier,
                    navHostController = navHostController
                )
            }

            fun NavHostController.navigateToBasket() = navigate(route = route)
            fun NavDestination.isBasket(): Boolean = this.route == route
        }

        object Profile: Main("Profile") {
            override val navigationTab: NavigationTab = NavigationTab(
                route = route,
                displayName = R.string.profile,
                imageVector = Icons.Default.Person,
                badgeFlow = badge.asStateFlow(),
                navigate = { navigateToProfile() }
            ) { modifier, navHostController, _ ->
                ProfileScreen(
                    modifier = modifier,
                    navHostController = navHostController
                )
            }

            fun NavHostController.navigateToProfile() = navigate(route = route)
            fun NavDestination.isProfile(): Boolean = this.route == route
        }

    }
}