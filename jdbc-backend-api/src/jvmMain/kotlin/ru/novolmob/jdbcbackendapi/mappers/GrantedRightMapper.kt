package ru.novolmob.jdbcbackendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.GrantedRights
import java.sql.ResultSet

class GrantedRightMapper: Mapper<ResultSet, GrantedRightModel> {
    override fun invoke(input: ResultSet): Either<AbstractBackendException, GrantedRightModel> =
        Either.backend {
            GrantedRightModel(
                id = input get GrantedRights.id,
                workerId = input get GrantedRights.workerId,
                code = input get GrantedRights.code,
                adminId = input get GrantedRights.adminId
            )
        }
}