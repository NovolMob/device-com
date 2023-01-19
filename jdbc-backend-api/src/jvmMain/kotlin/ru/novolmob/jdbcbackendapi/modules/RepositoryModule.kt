package ru.novolmob.jdbcbackendapi.modules

import org.koin.dsl.module
import ru.novolmob.backendapi.repositories.*
import ru.novolmob.backendapi.transformations.PasswordTransformation
import ru.novolmob.jdbcbackendapi.mappers.*
import ru.novolmob.jdbcbackendapi.repositories.*
import ru.novolmob.jdbcbackendapi.repositories.details.*

val repositoryModule = module {
    single<IUserRepository> {
        UserRepositoryImpl(
            mapper = get<UserMapper>(),
            passwordTransformation = PasswordTransformation()
        )
    }
    single<IGrantedRightRepository> {
        GrantedRightRepositoryImpl(
            mapper = get<GrantedRightMapper>()
        )
    }
    single<ICatalogRepository> {
        CatalogRepositoryImpl(
            mapper = get<DeviceShortModelMapper>()
        )
    }
    single<IBasketRepository> {
        BasketRepositoryImpl(
            mapper = get<BasketMapper>(),
            itemMapper = get<BasketItemMapper>(),
        )
    }
    single<ICityRepository> {
        CityRepositoryImpl(
            mapper = get<CityMapper>(),
            shortModelMapper = get<CityShortModelMapper>(),
            fullModelMapper = get<CityFullModelMapper>()
        )
    }
    single<IDeviceRepository> {
        DeviceRepositoryImpl(
            mapper = get<DeviceMapper>(),
            fullModelMapper = get<DeviceFullModelMapper>()
        )
    }
    single<IDeviceTypeRepository> {
        DeviceTypeRepositoryImpl(
            mapper = get<DeviceTypeMapper>(),
            shortModelMapper = get<DeviceTypeShortModelMapper>(),
            fullModelMapper = get<DeviceTypeFullModelMapper>()
        )
    }
    single<IPointRepository> {
        PointRepositoryImpl(
            mapper = get<PointMapper>(),
            shortModelMapper = get<PointShortModelMapper>(),
            fullModelMapper = get<PointFullModelMapper>()
        )
    }
    single<IOrderToStatusRepository> {
        OrderToStatusRepositoryImpl(
            shortModelMapper = get<OrderStatusShortModelMapper>(),
            fullModelMapper = get<OrderStatusFullModelMapper>()
        )
    }
    single<IOrderToDeviceRepository> {
        OrderToDeviceRepositoryImpl(
            mapper = get<OrderItemMapper>(),
        )
    }
    single<IOrderRepository> {
        OrderRepositoryImpl(
            blankFullModelMapper = get<BlankOrderFullModelMapper>(),
            blankShortModelMapper = get<BlankOrderShortModelMapper>(),
            pointRepository = get(),
            orderToStatusRepository = get(),
            orderToDeviceRepository = get()
        )
    }
    single<IWorkerRepository> {
        WorkerRepositoryImpl(
            mapper = get<WorkerMapper>(),
            passwordTransformation = PasswordTransformation()
        )
    }
    single<ICityDetailRepository> {
        CityDetailRepositoryImpl(
            mapper = get<CityDetailMapper>(),
        )
    }
    single<IDeviceDetailRepository> {
        DeviceDetailRepositoryImpl(
            mapper = get<DeviceDetailMapper>(),
        )
    }
    single<IDeviceTypeDetailRepository> {
        DeviceTypeDetailRepositoryImpl(
            mapper = get<DeviceTypeDetailMapper>(),
        )
    }
    single<IOrderStatusDetailRepository> {
        OrderStatusDetailDetailRepositoryImpl(
            mapper = get<OrderStatusDetailMapper>(),
        )
    }
    single<IPointDetailRepository> {
        PointDetailRepositoryImpl(
            mapper = get<PointDetailMapper>(),
        )
    }
}