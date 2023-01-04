package ru.novolmob.user_mobile_app.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.user_mobile_app.services.*

val serviceModule = module {
    singleOf(::BasketServiceImpl).bind<IBasketService>().bind<IService>()
    singleOf(::CatalogServiceImpl).bind<ICatalogService>().bind<IService>()
    singleOf(::ProfileServiceImpl).bind<IProfileService>().bind<IService>()
    singleOf(::DevicesServiceImpl).bind<IDevicesService>().bind<IService>()
}