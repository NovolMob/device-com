package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderToDeviceEntityModel
import ru.novolmob.exposeddatabase.tables.OrderToDeviceTable

class ResultRowOrderToDeviceEntityMapper: Mapper<ResultRow, OrderToDeviceEntityModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, OrderToDeviceEntityModel> =
        OrderToDeviceEntityModel(
            id = input[OrderToDeviceTable.id].value,
            orderId = input[OrderToDeviceTable.order].value,
            deviceId = input[OrderToDeviceTable.device].value,
            amount = input[OrderToDeviceTable.amount],
            priceForOne = input[OrderToDeviceTable.priceForOne]
        ).right()
}