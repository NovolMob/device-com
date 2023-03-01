package ru.novolmob.devicecom.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.novolmob.devicecom.ui.authorization.AuthorizationViewModel
import ru.novolmob.devicecom.ui.basket.BasketViewModel
import ru.novolmob.devicecom.ui.catalog.CatalogViewModel
import ru.novolmob.devicecom.ui.device.DeviceViewModel
import ru.novolmob.devicecom.ui.order.OrdersViewModel
import ru.novolmob.devicecom.ui.profile.ProfileViewModel
import ru.novolmob.devicecom.ui.registration.RegistrationViewModel

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
            orderService = get(),
            profileService = get()
        )
    }
}