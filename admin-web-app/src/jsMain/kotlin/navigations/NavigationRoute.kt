package navigations

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backendapi.models.CityModel
import ru.novolmob.backendapi.models.WorkerModel
import screens.components.tableWithContent
import screens.loginScreen
import screens.workerScreen
import services.ICityService
import services.IProfileService
import services.IWorkerService
import styles.Colors

sealed class NavigationRoute: KoinComponent {
    protected val navigationScope: CoroutineScope
        get() = CoroutineScope(SupervisorJob())
    @Composable
    abstract fun screen()
    object Login: NavigationRoute() {
        private val profileService: IProfileService by inject()
        @Composable
        override fun screen() {
            loginScreen(profileService)
        }
    }

    object Main: NavigationRoute() {
        private val profileService: IProfileService by inject()
        private val tab = MutableStateFlow<MainNavigationRoute?>(MainNavigationRoute.Workers)
        private val tabs by lazy {
            listOf(
                MainNavigationRoute.Devices,
                MainNavigationRoute.Workers,
                MainNavigationRoute.Cities
            )
        }
        private val availableTabs = MutableStateFlow(listOf<MainNavigationRoute>())

        init {
            navigationScope.launch {
                launch {
                    combine(tabs.map { it.available }) { it }.collectLatest {
                        val new = it.mapIndexedNotNull { index, b -> if (b) tabs[index] else null }
                        availableTabs.update { new }
                    }
                }
                launch {
                    tab.collectLatest {
                        it?.available?.collectLatest { available ->
                            if (!available) tab.update { availableTabs.value.firstOrNull() }
                        }
                    }
                }
            }
        }

        fun navigate(tab: MainNavigationRoute) = this.tab.update { tab }

        @Composable
        override fun screen() {
            val tabs by availableTabs.collectAsState()
            val current by tab.collectAsState()
            val currentNotNull by remember(current) {
                derivedStateOf { current != null }
            }

            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Flex)
                        justifyContent(JustifyContent.Center)
                        alignItems(AlignItems.Center)
                        margin(10.px)
                    }
                }
            ) {
                Div(
                    attrs = {
                        style {
                            backgroundColor(Colors.closeButtonBackground)
                            padding(10.px)
                            display(DisplayStyle.Flex)
                            justifyContent(JustifyContent.Center)
                            alignItems(AlignItems.Center)
                            marginRight(10.px)
                        }
                        onClick {
                            profileService.logout()
                        }
                    }
                ) {
                    Img(src = "./logout.png", attrs = {
                        style {
                            width(32.px)
                            height(32.px)
                        }
                    })
                }
                tabs.forEach { route ->
                    val selected by remember(current) {
                        derivedStateOf { route == current }
                    }

                    Div(
                        attrs = {
                            style {
                                height(0.percent)
                                border(1.px, LineStyle.Solid, Colors.tabBorderColor)
                                padding(10.px)
                                if (selected) {
                                    backgroundColor(Colors.selectedTabBackground)
                                    color(Colors.selectedTabColor)
                                } else {
                                    backgroundColor(Colors.generalBackground)
                                    color(Colors.generalColor)
                                }
                            }
                            onClick { navigate(route) }
                        }
                    ) {
                        Text(route.title)
                    }
                }
            }
            if (currentNotNull) current!!.screen()
        }
    }
    sealed class MainNavigationRoute(title: String? = null): NavigationRoute() {
        open val title: String = title ?: this::class.simpleName ?: ""
        private val _available = MutableStateFlow(false)
        val available: StateFlow<Boolean> = _available.asStateFlow()

        fun available(value: Boolean) = navigationScope.launch { _available.update { value } }

        object Devices: MainNavigationRoute() {
            @Composable
            override fun screen() {
                TODO("Not yet implemented")
            }
        }

        object Workers: MainNavigationRoute("Работники") {
            private val workerService: IWorkerService by inject()
            private val selectedWorker = MutableStateFlow<WorkerModel?>(null)

            init {
                navigationScope.launch {
                    workerService.update()
                }
            }

            fun select(workerModel: WorkerModel) = navigationScope.launch { selectedWorker.update { workerModel } }
            fun reset() = navigationScope.launch { selectedWorker.update { null } }

            @Composable
            override fun screen() {
                val workers by workerService.workers.collectAsState()
                val selected by selectedWorker.collectAsState()

                tableWithContent(
                    list = workers,
                    key = { it.id },
                    selected = selected,
                    onClickTableCell = { select(it) },
                    convert = {
                        mapOf(
                            "uuid" to id,
                            "Пункт выдачи" to pointId,
                            "Имя" to firstname,
                            "Фамилия" to lastname,
                            "Отчество" to patronymic,
                            "Язык" to language,
                            "Электронная почта" to email,
                            "Номер телефона" to phoneNumber
                        )
                    }
                ) {
                    workerScreen(workerService, it)
                }
            }
        }

        object Cities: MainNavigationRoute("Города") {
            private val cityService: ICityService by inject()
            private val selectedCity = MutableStateFlow<CityModel?>(null)

            init {
                navigationScope.launch {
                    cityService.update()
                }
            }

            fun select(cityModel: CityModel) = navigationScope.launch { selectedCity.update { cityModel } }
            fun reset() = navigationScope.launch { selectedCity.update { null } }

            @Composable
            override fun screen() {
                val cities by cityService.cities.collectAsState()
                val selected by selectedCity.collectAsState()

                tableWithContent(
                    list = cities,
                    key = { it.id },
                    selected = selected,
                    onClickTableCell = { select(it) },
                    convert = {
                        mapOf(
                            "uuid" to id,
                        )
                    }
                ) {

                }
            }
        }
    }

}