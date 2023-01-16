package naavigations

import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import screens.loginScreen
import services.IProfileService

sealed class NavigationRoute {
    @Composable
    abstract fun screen()
    object Login: KoinComponent, NavigationRoute() {
        private val profileService: IProfileService by inject()
        @Composable
        override fun screen() {
            loginScreen(profileService)
        }
    }

    class Main(val tab: MainNavigationRoute): NavigationRoute() {
        @Composable
        override fun screen() {

        }
    }
    sealed class MainNavigationRoute: NavigationRoute() {
        object Devices: MainNavigationRoute() {
            @Composable
            override fun screen() {
                TODO("Not yet implemented")
            }
        }
    }

}