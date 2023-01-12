package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.failedToCreateWorker
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.GrantedRightCreateModel
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.backendapi.repositories.IGrantedRightRepository
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.functions.CreationOrUpdateTableFunction
import ru.novolmob.jdbcdatabase.tables.GrantedRights
import java.sql.ResultSet

class GrantedRightRepositoryImpl(
    val mapper: Mapper<ResultSet, GrantedRightModel>
): IGrantedRightRepository {
    override suspend fun getAllRightsFor(workerId: WorkerId): Either<AbstractBackendException, List<GrantedRightModel>> =
        GrantedRights.getAllRightsFor(workerId) { list(mapper) }

    override suspend fun contains(workerId: WorkerId, code: Code): Either<AbstractBackendException, Boolean> =
        GrantedRights.contains(workerId, code).right()

    override suspend fun removeFor(workerId: WorkerId, code: Code): Either<AbstractBackendException, Boolean> =
        (GrantedRights.remove(workerId, code) > 0).right()

    override suspend fun post(createModel: GrantedRightCreateModel): Either<AbstractBackendException, GrantedRightModel> =
        CreationOrUpdateTableFunction.CreationOrUpdateGranterRightFunction.call(
            workerId = createModel.workerId,
            code = createModel.code,
            adminId = createModel.adminId
        ) { fold(ifEmpty = { failedToCreateWorker() }, mapper::invoke) }

}