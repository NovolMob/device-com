package ru.novolmob.user_mobile_app.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.novolmob.user_mobile_app.ui.authorization.AuthorizationViewModel
import ru.novolmob.user_mobile_app.ui.basket.BasketViewModel
import ru.novolmob.user_mobile_app.ui.catalog.CatalogViewModel
import ru.novolmob.user_mobile_app.ui.device.DeviceViewModel
import ru.novolmob.user_mobile_app.ui.order.OrdersViewModel
import ru.novolmob.user_mobile_app.ui.profile.ProfileViewModel
import ru.novolmob.user_mobile_app.ui.registration.RegistrationViewModel

val viewModelModule = module {
    viewModel {
        AuthorizationViewModel(
            profileService = get()
        )
    }
    viewModel {
        RegistrationViewModel(
            profileService = get(),
            cityService = get()
        )
    }
    viewModel {
        CatalogViewModel(
            basketService = get(),
            catalogService = get(),
            devicesService = get()
        )
    }
    viewModel {
        ProfileViewModel(
            profileService = get(),
            cityService = get()
        )
    }
    viewModel {
        BasketViewModel(
            basketService = get(),
            pointService = get(),
            orderService = get()
        )
    }
    viewModel {
        DeviceViewModel(
            deviceService = get(),
            basketService = get()
        )
    }
    viewModel {
        OrdersViewModel(
            orderService = get()
        )
    }
}