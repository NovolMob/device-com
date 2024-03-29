package ru.novolmob.devicecom.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.novolmob.devicecom.R
import ru.novolmob.devicecom.models.NavigationTab
import ru.novolmob.devicecom.ui.basket.BasketScreen
import ru.novolmob.devicecom.ui.catalog.CatalogScreen
import ru.novolmob.devicecom.ui.device.DeviceScreen
import ru.novolmob.devicecom.ui.order.OrdersScreen
import ru.novolmob.devicecom.ui.profile.ProfileScreen

sealed class NavigationRoute(route: String? = null) {
    open val route: String = route ?: ""

    object Authorization: NavigationRoute("Authorization") {
        fun NavHostController.navigateToAuthorization() = navigate(route = route)
    }

    object Registration: NavigationRoute("Registration") {
        fun NavHostController.navigateToRegistration() = navigate(route = route)
    }

    sealed class Main(route: String? = null): NavigationRoute() {
        override val route: String = Companion.route + (route?.let { "/$it" } ?: "")
        protected val badge = MutableStateFlow(0)
        abstract val navigationTab: NavigationTab?

        suspend fun badge(value: Int) = badge.emit(value)

        companion object: NavigationRoute("Main") {
            val tabs = listOf(Profile, Catalog, Basket, Orders)

            fun NavHostController.navigateToMain() = navigate(route = route)
        }

        sealed class Catalog(route: String? = null): Main() {
            override val route: String = Companion.route + (route?.let { "/$it" } ?: "")

            companion object: Main("Catalog") {
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
                    navigate = {
                        navigateToCatalog()
                    }
                ) { modifier, navHostController, _ ->
                    CatalogScreen(
                        modifier = modifier,
                        navHostController = navHostController
                    )
                }

                fun NavHostController.navigateToCatalog(builder: NavOptionsBuilder.() -> Unit = {}) =
                    navigate(route = route) {
                        builder()
                        popUpTo(graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

            }

            object Device: Catalog("Device") {
                override val navigationTab: NavigationTab  = NavigationTab(
                    route = route,
                    arguments = listOf(position(1)),
                    displayName = R.string.device,
                ) { modifier, navHostController, _ ->
                    DeviceScreen(
                        modifier = modifier,
                        navHostController = navHostController
                    )
                }

                fun NavHostController.navigateToDevice(builder: NavOptionsBuilder.() -> Unit  = {}) = navigate(route = route, builder)
            }

        }

        object Basket: Main("Basket") {
            override val navigationTab: NavigationTab = NavigationTab(
                route = route,
                displayName = R.string.basket,
                imageVector = Icons.Rounded.ShoppingCart,
                badgeFlow = badge.asStateFlow(),
                navigate = {
                    navigateToBasket()
                }
            ) { modifier, navHostController, _ ->
                BasketScreen(
                    modifier = modifier,
                    navHostController = navHostController
                )
            }

            fun NavHostController.navigateToBasket(builder: NavOptionsBuilder.() -> Unit  = {}) = navigate(route = route) {
                builder()
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        sealed class Orders(route: String? = null): Main() {
            override val route: String = Companion.route + (route?.let { "/$it" } ?: "")
            companion object: Main("Orders") {
                override val navigationTab: NavigationTab = NavigationTab(
                    route = route,
                    displayName = R.string.orders,
                    imageVectorId = R.drawable.all_inbox,
                    badgeFlow = badge.asStateFlow(),
                    navigate = {
                        navigateToOrders()
                    }
                ) { modifier, navHostController, _ ->
                    OrdersScreen(
                        modifier = modifier,
                        navHostController = navHostController
                    )
                }

                fun NavHostController.navigateToOrders(builder: NavOptionsBuilder.() -> Unit  = {}) = navigate(route = route) {
                    builder()
                    popUpTo(graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            object Order: Orders("Order") {
                override val navigationTab: NavigationTab  = NavigationTab(
                    route = route,
                    displayName = R.string.order,
                    arguments = listOf(position(3))
                ) { modifier, navHostController, _ ->
                    DeviceScreen(
                        modifier = modifier,
                        navHostController = navHostController
                    )
                }

                fun NavHostController.navigateToOrder(builder: NavOptionsBuilder.() -> Unit  = {}) = navigate(route = route, builder)
            }
        }

        object Profile: Main("Profile") {
            override val navigationTab: NavigationTab = NavigationTab(
                route = route,
                displayName = R.string.profile,
                imageVector = Icons.Default.Person,
                badgeFlow = badge.asStateFlow(),
                navigate = {
                    navigateToProfile()
                }
            ) { modifier, navHostController, _ ->
                ProfileScreen(
                    modifier = modifier,
                    navHostController = navHostController
                )
            }

            fun NavHostController.navigateToProfile(builder: NavOptionsBuilder.() -> Unit  = {}) = navigate(route = route) {
                builder()
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

    }
}