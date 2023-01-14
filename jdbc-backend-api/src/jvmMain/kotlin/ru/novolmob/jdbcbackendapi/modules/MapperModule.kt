package ru.novolmob.jdbcbackendapi.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.novolmob.jdbcbackendapi.mappers.*

val mapperModule = module {
    singleOf(::UserMapper)
    singleOf(::GrantedRightMapper)
    singleOf(::DeviceShortModelMapper)
    singleOf(::BasketMapper)
    singleOf(::BasketItemMapper)
    singleOf(::CityMapper)
    singleOf(::CityShortModelMapper)
    singleOf(::CityFullModelMapper)
    singleOf(::DeviceMapper)
    singleOf(::DeviceFullModelMapper)
    singleOf(::DeviceTypeMapper)
    singleOf(::DeviceTypeShortModelMapper)
    singleOf(::DeviceTypeFullModelMapper)
    singleOf(::PointMapper)
    singleOf(::PointShortModelMapper)
    singleOf(::PointFullModelMapper)
    singleOf(::OrderStatusFullModelMapper)
    singleOf(::OrderStatusShortModelMapper)
    singleOf(::OrderItemMapper)
    singleOf(::BlankOrderShortModelMapper)
    singleOf(::BlankOrderFullModelMapper)
}