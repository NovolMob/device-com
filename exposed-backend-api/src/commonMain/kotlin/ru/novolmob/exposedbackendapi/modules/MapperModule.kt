package ru.novolmob.exposedbackendapi.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.novolmob.exposedbackendapi.mappers.*

val mapperModule = module {
    singleOf(::BasketMapper)
    singleOf(::CityDetailMapper)
    singleOf(::CityMapper)
    singleOf(::DeviceDetailMapper)
    singleOf(::DeviceMapper)
    singleOf(::DeviceTypeDetailMapper)
    singleOf(::DeviceTypeMapper)
    singleOf(::GrantedRightMapper)
    singleOf(::OrderMapper)
    singleOf(::OrderStatusDetailMapper)
    singleOf(::OrderStatusMapper)
    singleOf(::OrderToDeviceMapper)
    singleOf(::OrderToStatusMapper)
    singleOf(::PointDetailMapper)
    singleOf(::PointMapper)
    singleOf(::ResultRowBasketMapper)
    singleOf(::ResultRowCityDetailMapper)
    singleOf(::ResultRowCityMapper)
    singleOf(::ResultRowDeviceDetailMapper)
    singleOf(::ResultRowDeviceMapper)
    singleOf(::ResultRowDeviceTypeDetailMapper)
    singleOf(::ResultRowDeviceTypeMapper)
    singleOf(::ResultRowGrantedRightMapper)
    singleOf(::ResultRowOrderMapper)
    singleOf(::ResultRowOrderStatusDetailMapper)
    singleOf(::ResultRowOrderStatusMapper)
    singleOf(::ResultRowOrderToDeviceEntityMapper)
    singleOf(::ResultRowOrderToStatusMapper)
    singleOf(::ResultRowPointDetailMapper)
    singleOf(::ResultRowPointMapper)
    singleOf(::ResultRowUserInfoMapper)
    singleOf(::ResultRowWorkerInfoMapper)
    singleOf(::UserCredentialMapper)
    singleOf(::UserMapper)
    singleOf(::WorkerCredentialMapper)
    singleOf(::WorkerMapper)
}