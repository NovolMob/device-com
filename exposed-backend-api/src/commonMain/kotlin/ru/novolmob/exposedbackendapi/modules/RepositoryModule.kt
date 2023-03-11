package ru.novolmob.exposedbackendapi.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.backendapi.repositories.*
import ru.novolmob.exposedbackendapi.mappers.*
import ru.novolmob.exposedbackendapi.repositories.*
import ru.novolmob.exposedbackendapi.repositories.credentials.UserCredentialRepositoryImpl
import ru.novolmob.exposedbackendapi.repositories.credentials.WorkerCredentialRepositoryImpl
import ru.novolmob.exposedbackendapi.repositories.details.*

val repositoryModule = module {
    //details
    single<ICityDetailRepository> {
        CityDetailRepositoryImpl(
            mapper = get<CityDetailMapper>(),
            resultRowMapper = get<ResultRowCityDetailMapper>()
        )
    }

    single<IDeviceDetailRepository> {
        DeviceDetailRepositoryImpl(
            mapper = get<DeviceDetailMapper>(),
            resultRowMapper = get<ResultRowDeviceDetailMapper>()
        )
    }

    single<IDeviceTypeDetailRepository> {
        DeviceTypeDetailRepositoryImpl(
            mapper = get<DeviceTypeDetailMapper>(),
            resultRowMapper = get<ResultRowDeviceTypeDetailMapper>()
        )
    }

    single<IOrderStatusDetailRepository> {
        OrderStatusDetailRepositoryImpl(
            mapper = get<OrderStatusDetailMapper>(),
            resultRowMapper = get<ResultRowOrderStatusDetailMapper>()
        )
    }

    single<IPointDetailRepository> {
        PointDetailRepositoryImpl(
            mapper = get<PointDetailMapper>(),
            resultRowMapper = get<ResultRowPointDetailMapper>()
        )
    }


    //n to n tables
    single<IOrderToDeviceRepository> {
        OrderToDeviceRepositoryImpl(
            mapper = get<OrderToDeviceMapper>(),
            resultRowMapper = get<ResultRowOrderToDeviceEntityMapper>(),
        )
    }

    single<IOrderToStatusRepository> {
        OrderToStatusRepositoryImpl(
            mapper = get<OrderToStatusMapper>(),
            resultRowMapper = get<ResultRowOrderToStatusMapper>(),
            orderStatusDetailRepository = get()
        )
    }


    //simple
    single<IBasketRepository> {
        BasketRepositoryImpl(
            mapper = get<BasketMapper>(),
            resultRowMapper = get<ResultRowBasketMapper>(),
            deviceDetailRepository = get()
        )
    }

    singleOf(::CatalogRepositoryImpl).bind<ICatalogRepository>()

    single<ICityRepository> {
        CityRepositoryImpl(
            mapper = get<CityMapper>(),
            resultRowMapper = get<ResultRowCityMapper>(),
            cityDetailRepository = get()
        )
    }

    single<IDeviceTypeRepository> {
        DeviceTypeRepositoryImpl(
            mapper = get<DeviceTypeMapper>(),
            resultRowMapper = get<ResultRowDeviceTypeMapper>(),
            deviceTypeDetailRepository = get()
        )
    }

    single<IGrantedRightRepository> {
        GrantedRightRepositoryImpl(
            mapper = get<GrantedRightMapper>(),
            resultRowMapper = get<ResultRowGrantedRightMapper>()
        )
    }

    single<IOrderStatusRepository> {
        OrderStatusRepositoryImpl(
            mapper = get<OrderStatusMapper>(),
            resultRowMapper = get<ResultRowOrderStatusMapper>(),
        )
    }

    single<IPointRepository> {
        PointRepositoryImpl(
            mapper = get<PointMapper>(),
            resultRowMapper = get<ResultRowPointMapper>(),
            cityRepository = get(),
            pointDetailRepository = get()
        )
    }

    single<IUserCredentialRepository> {
        UserCredentialRepositoryImpl(
            mapper = get<UserCredentialMapper>(),
        )
    }

    single<IWorkerCredentialRepository> {
        WorkerCredentialRepositoryImpl(
            mapper = get<WorkerCredentialMapper>(),
        )
    }


    //strong
    single<IDeviceRepository> {
        DeviceRepositoryImpl(
            mapper = get<DeviceMapper>(),
            resultRowMapper = get<ResultRowDeviceMapper>(),
            deviceDetailRepository = get(),
            deviceTypeRepository = get()
        )
    }

    single<IOrderRepository> {
        OrderRepositoryImpl(
            mapper = get<OrderMapper>(),
            resultRowMapper = get<ResultRowOrderMapper>(),
            deviceDetailRepository = get(),
            pointRepository = get(),
            orderToDeviceRepository = get(),
            orderToStatusRepository = get()
        )
    }

    single<IUserRepository> {
        UserRepositoryImpl(
            mapper = get<UserMapper>(),
            resultRowInfoMapper = get<ResultRowUserInfoMapper>(),
            userCredentialRepository = get()
        )
    }

    single<IWorkerRepository> {
        WorkerRepositoryImpl(
            mapper = get<WorkerMapper>(),
            resultRowInfoMapper = get<ResultRowWorkerInfoMapper>(),
            workerCredentialRepository = get()
        )
    }



}