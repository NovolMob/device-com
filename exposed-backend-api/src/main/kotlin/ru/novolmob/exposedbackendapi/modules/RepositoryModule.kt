package ru.novolmob.exposedbackendapi.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.backendapi.repositories.*
import ru.novolmob.exposedbackendapi.mappers.*
import ru.novolmob.exposedbackendapi.repositories.*

val repositoryModule = module {
    //details
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
            itemMapper = get<OrderToDeviceItemMapper>(),
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

    single<IPointToDeviceRepository> {
        PointToDeviceRepositoryImpl(
            mapper = get<PointToDeviceMapper>(),
            itemMapper = get<PointToDeviceItemMapper>(),
            resultRowMapper = get<ResultRowPointToDeviceMapper>(),
            pointDetailRepository = get()
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
            pointDetailRepository = get()
        )
    }

    single<IUserCredentialRepository> {
        UserCredentialRepositoryImpl(
            mapper = get<UserCredentialMapper>(),
            resultRowMapper = get<ResultRowUserCredentialMapper>()
        )
    }

    single<IWorkerCredentialRepository> {
        WorkerCredentialRepositoryImpl(
            mapper = get<WorkerCredentialMapper>(),
            resultRowMapper = get<ResultRowWorkerCredentialMapper>()
        )
    }


    //strong
    single<IDeviceRepository> {
        DeviceRepositoryImpl(
            mapper = get<DeviceMapper>(),
            resultRowMapper = get<ResultRowDeviceMapper>(),
            deviceDetailRepository = get(),
            deviceTypeRepository = get(),
            pointToDeviceRepository = get()
        )
    }

    single<IOrderRepository> {
        OrderRepositoryImpl(
            mapper = get<OrderMapper>(),
            resultRowMapper = get<ResultRowOrderMapper>(),
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