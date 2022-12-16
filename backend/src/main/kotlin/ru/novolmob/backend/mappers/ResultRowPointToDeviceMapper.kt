package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.PointToDeviceEntityModel
import ru.novolmob.database.tables.PointToDeviceTable

class ResultRowPointToDeviceMapper: Mapper<ResultRow, PointToDeviceEntityModel> {
    override fun invoke(input: ResultRow): Either<BackendException, PointToDeviceEntityModel> =
        PointToDeviceEntityModel(
            id = input[PointToDeviceTable.id].value,
            pointId = input[PointToDeviceTable.point].value,
            deviceId = input[PointToDeviceTable.device].value,
            amount = input[PointToDeviceTable.amount]
        ).right()
}