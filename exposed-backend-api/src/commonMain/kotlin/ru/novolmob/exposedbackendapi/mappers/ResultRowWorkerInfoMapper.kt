package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.WorkerInfoModel
import ru.novolmob.exposeddatabase.tables.Workers

class ResultRowWorkerInfoMapper: Mapper<ResultRow, WorkerInfoModel> {
    override fun invoke(input: ResultRow): Either<AbstractBackendException, WorkerInfoModel> =
        WorkerInfoModel(
            id = input[Workers.id].value,
            pointId = input[Workers.point]?.value,
            firstname = input[Workers.firstname],
            lastname = input[Workers.lastname],
            patronymic = input[Workers.patronymic],
            language = input[Workers.language]
        ).right()
}