package ru.novolmob.backend.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.database.tables.GrantedRights

class ResultRowGrantedRightMapper: Mapper<ResultRow, GrantedRightModel> {
    override fun invoke(input: ResultRow): Either<BackendException, GrantedRightModel> =
        GrantedRightModel(
            id = input[GrantedRights.id].value,
            workerId = input[GrantedRights.worker].value,
            code = input[GrantedRights.code],
            adminId = input[GrantedRights.admin].value
        ).right()
}