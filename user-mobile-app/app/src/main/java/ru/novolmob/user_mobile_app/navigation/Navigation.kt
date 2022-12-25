@file:OptIn(ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
                tabs = NavigationRoute.Main.tabs.map { it.navigationTab }
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
                    EnterTransition.None
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
                    ExitTransition.None
            }
        },
        popEnterTransition = {
            tabs.compare(initialState, targetState).let {
                if (it < 0)
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                else if (it > 0)
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                else
                    EnterTransition.None
            }
        },
        popExitTransition = {
            tabs.compare(initialState, targetState).let {
                if (it < 0)
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                else if (it > 0)
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                else
                    ExitTransition.None
            }
        }
    ) {
        tabs.forEach { composable(modifier, navHostController, it) }
    }
}

private fun List<NavigationTab>.compare(first: NavBackStackEntry, second: NavBackStackEntry): Int = indexOf(first).compareTo(indexOf(second))

private fun List<NavigationTab>.indexOf(entry: NavBackStackEntry): Int = indexOfFirst { it.route == entry.destination.route }

private fun NavGraphBuilder.composable(modifier: Modifier, navHostController: NavHostController, tab: NavigationTab) =
    composable(route = tab.route, arguments = tab.arguments, deepLinks = tab.deepLinks) {
        tab.content(this, modifier, navHostController, it)
    }

