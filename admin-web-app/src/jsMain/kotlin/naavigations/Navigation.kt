package naavigations

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object Navigation {
    private val _current = MutableStateFlow<NavigationRoute>(NavigationRoute.Login)
    val current = _current.asStateFlow()

    fun toLogin() {
        _current.update { NavigationRoute.Login }
    }

    fun toMain(tab: NavigationRoute.MainNavigationRoute) {
        _current.update { NavigationRoute.Main(tab) }
    }

    fun toMain() {
        toDevices()
    }

    fun toDevices() {
        toMain(NavigationRoute.MainNavigationRoute.Devices)
    }

}