@file:OptIn(ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import ru.novolmob.user_mobile_app.models.NavigationTab
import ru.novolmob.user_mobile_app.ui.authorization.AuthorizationScreen
import ru.novolmob.user_mobile_app.ui.main.MainScreen
import ru.novolmob.user_mobile_app.ui.registration.RegistrationScreen

@Composable
fun GeneralNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = NavigationRoute.Authorization.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(route = NavigationRoute.Authorization.route,) {
            AuthorizationScreen(modifier = modifier, navHostController = navHostController)
        }
        composable(route = NavigationRoute.Registration.route) {
            RegistrationScreen(modifier = modifier, navHostController = navHostController)
        }
        composable(route = NavigationRoute.Main.route) {
            MainScreen(
                modifier = modifier,
                navHostController = navHostController,
                tabs = NavigationRoute.Main.tabs.mapNotNull { it.navigationTab }
            )
        }
    }
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    startDestination: NavigationRoute.Main,
    navHostController: NavHostController = rememberAnimatedNavController(),
    tabs: List<NavigationTab>
) {
    AnimatedNavHost(
        navController = navHostController,
        startDestination = startDestination.route,
        enterTransition = {
            tabs.compare(initialState, targetState).let {
                if (it < 0)
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                else if (it > 0)
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                else
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Up,
                        animationSpec = tween(300)
                    )
            }
        },
        exitTransition = {
            tabs.compare(initialState, targetState).let {
                if (it < 0)
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                else if (it > 0)
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                else
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Up,
                        animationSpec = tween(300)
                    )
            }
        },
        popEnterTransition = {
            tabs.compare(initialState, targetState).let {
                if (it > 0)
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                else if (it < 0)
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                else
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Down,
                        animationSpec = tween(300)
                    )
            }
        },
        popExitTransition = {
            tabs.compare(initialState, targetState).let {
                if (it > 0)
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                else if (it < 0)
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                else
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Down,
                        animationSpec = tween(300)
                    )
            }
        }
    ) {
        tabs.forEach { composable(modifier, navHostController, it) }
        composable(modifier, navHostController, NavigationRoute.Main.Catalog.Device.navigationTab)
    }
}

private fun List<NavigationTab>.compare(first: NavBackStackEntry, second: NavBackStackEntry): Int = indexOf(first).compareTo(indexOf(second))

private fun List<NavigationTab>.indexOf(entry: NavBackStackEntry): Int = indexOfFirst { it.route == entry.destination.route }.takeIf { it >= 0 } ?: entry.position()

private fun NavBackStackEntry.position(): Int = arguments?.getInt("position") ?: -1
fun position(value: Int) = navArgument("position") {
    type = NavType.IntType
    defaultValue = value
}

private fun NavGraphBuilder.composable(modifier: Modifier, navHostController: NavHostController, tab: NavigationTab) =
    composable(route = tab.route, arguments = tab.arguments, deepLinks = tab.deepLinks) {
        tab.content(this, modifier, navHostController, it)
    }

