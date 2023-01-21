package ru.novolmob.devicecom.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.devicecom.repositories.*

val repositoryModule = module {
    singleOf(::UserRepositoryImpl).bind<IUserRepository>()
    singleOf(::CatalogRepositoryImpl).bind<ICatalogRepository>()
    singleOf(::BasketRepositoryImpl).bind<IBasketRepository>()
    singleOf(::DeviceRepositoryImpl).bind<IDeviceRepository>()
    singleOf(::CityRepositoryImpl).bind<ICityRepository>()
    singleOf(::PointRepositoryImpl).bind<IPointRepository>()
    singleOf(::OrderRepositoryImpl).bind<IOrderRepository>()
}