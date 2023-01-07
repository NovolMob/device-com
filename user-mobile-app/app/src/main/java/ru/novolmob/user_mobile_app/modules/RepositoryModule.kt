package ru.novolmob.user_mobile_app.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.user_mobile_app.repositories.*

val repositoryModule = module {
    singleOf(::UserRepositoryImpl).bind<IUserRepository>()
    singleOf(::CatalogRepositoryImpl).bind<ICatalogRepository>()
    singleOf(::BasketRepositoryImpl).bind<IBasketRepository>()
    singleOf(::DeviceRepositoryImpl).bind<IDeviceRepository>()
    singleOf(::CityRepositoryImpl).bind<ICityRepository>()
    singleOf(::PointRepositoryImpl).bind<IPointRepository>()
    singleOf(::OrderRepositoryImpl).bind<IOrderRepository>()
}