package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.workerByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.GrantedRightCreateModel
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.backendapi.repositories.IGrantedRightRepository
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.exposeddatabase.entities.GrantedRight
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.exposeddatabase.tables.GrantedRights

class GrantedRightRepositoryImpl(
    val mapper: Mapper<GrantedRight, GrantedRightModel>,
    val resultRowMapper: Mapper<ResultRow, GrantedRightModel>
): IGrantedRightRepository {

    private fun find(workerId: WorkerId, code: Code): GrantedRight? =
        GrantedRight.find { (GrantedRights.worker eq workerId) and (GrantedRights.code eq code) }
            .limit(1).firstOrNull()

    override suspend fun getAllRightsFor(workerId: WorkerId): Either<AbstractBackendException, List<GrantedRightModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            GrantedRight.find { GrantedRights.worker eq workerId }
                .parTraverseEither { mapper(it) }
        }

    override suspend fun containsAny(workerId: WorkerId, codes: List<Code>): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            (!GrantedRight.find { GrantedRights.code inList codes }.empty()).right()
        }

    override suspend fun removeFor(workerId: WorkerId, code: Code): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(workerId, code)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

    override suspend fun post(createModel: GrantedRightCreateModel): Either<AbstractBackendException, GrantedRightModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            val admin = Worker.findById(createModel.adminId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.adminId).left()
            GrantedRight.new {
                this.worker = worker
                this.admin = admin
                this.code = createModel.code
            }.let(mapper::invoke)
        }
}