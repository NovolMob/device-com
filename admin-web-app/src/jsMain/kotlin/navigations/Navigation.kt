package navigations

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object Navigation {
    private val _current = MutableStateFlow<NavigationRoute>(NavigationRoute.Login)
    val current = _current.asStateFlow()

    fun toLogin() {
        _current.update { NavigationRoute.Login }
    }

    fun toMain() {
        _current.update { NavigationRoute.Main }
    }

    fun toDevices() {
        toMain()
        NavigationRoute.Main.navigate(NavigationRoute.MainNavigationRoute.Devices)
    }

    fun toWorkers() {
        toMain()
        NavigationRoute.Main.navigate(NavigationRoute.MainNavigationRoute.Workers)
    }

    fun toCities() {
        toMain()
        NavigationRoute.Main.navigate(NavigationRoute.MainNavigationRoute.Cities)
    }

}