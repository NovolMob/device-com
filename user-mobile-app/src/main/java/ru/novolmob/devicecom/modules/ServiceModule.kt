package ru.novolmob.devicecom.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.devicecom.services.*

val serviceModule = module {
    singleOf(::BasketServiceImpl).bind<IBasketService>().bind<IService>()
    singleOf(::CatalogServiceImpl).bind<ICatalogService>().bind<IService>()
    singleOf(::ProfileServiceImpl).bind<IProfileService>().bind<IService>()
    singleOf(::DevicesServiceImpl).bind<IDevicesService>().bind<IService>()
    singleOf(::CityServiceImpl).bind<ICityService>().bind<IService>()
    singleOf(::PointServiceImpl).bind<IPointService>().bind<IService>()
    singleOf(::OrderServiceImpl).bind<IOrderService>().bind<IService>()
}