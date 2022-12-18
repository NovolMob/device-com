package ru.novolmob.backend.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.grantedRightByIdNotFound
import ru.novolmob.backend.exceptions.workerByIdNotFound
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backend.util.RepositoryUtil
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IGrantedRightRepository
import ru.novolmob.exposeddatabase.entities.GrantedRight
import ru.novolmob.exposeddatabase.entities.Worker
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.ids.GrantedRightId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.exposeddatabase.tables.GrantedRights

class GrantedRightRepositoryImpl(
    val mapper: Mapper<GrantedRight, GrantedRightModel>,
    val resultRowMapper: Mapper<ResultRow, GrantedRightModel>
): IGrantedRightRepository {

    private fun find(workerId: WorkerId, code: Code): GrantedRight? =
        GrantedRight.find { (GrantedRights.worker eq workerId) and (GrantedRights.code eq code) }
            .limit(1).firstOrNull()

    override suspend fun getAllRightsFor(workerId: WorkerId): Either<BackendException, List<GrantedRightModel>> =
        newSuspendedTransaction(Dispatchers.IO) {
            GrantedRight.find { GrantedRights.worker eq workerId }
                .parTraverseEither { mapper(it) }
        }

    override suspend fun contains(workerId: WorkerId, code: Code): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            (find(workerId, code) != null).right()
        }

    override suspend fun removeFor(workerId: WorkerId, code: Code): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            find(workerId, code)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

    override suspend fun get(id: GrantedRightId): Either<BackendException, GrantedRightModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            GrantedRight.findById(id)?.let(mapper::invoke) ?: grantedRightByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<BackendException, Page<GrantedRightModel>> =
        RepositoryUtil.generalGatAll(GrantedRights, pagination, resultRowMapper)

    override suspend fun post(createModel: GrantedRightCreateModel): Either<BackendException, GrantedRightModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            val admin = Worker.findById(createModel.adminId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.adminId).left()
            GrantedRight.new {
                this.worker = worker
                this.admin = admin
                this.code = createModel.code
            }.let(mapper::invoke)
        }

    override suspend fun post(id: GrantedRightId, createModel: GrantedRightCreateModel): Either<BackendException, GrantedRightModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = Worker.findById(createModel.workerId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.workerId).left()
            val admin = Worker.findById(createModel.adminId) ?: return@newSuspendedTransaction workerByIdNotFound(createModel.adminId).left()
            GrantedRight.findById(id)?.apply {
                this.worker = worker
                this.admin = admin
                this.code = createModel.code
            }?.let(mapper::invoke) ?: grantedRightByIdNotFound(id).left()
        }

    override suspend fun put(id: GrantedRightId, updateModel: GrantedRightUpdateModel): Either<BackendException, GrantedRightModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val worker = updateModel.workerId?.let {
                Worker.findById(it) ?: return@newSuspendedTransaction workerByIdNotFound(it).left()
            }
            val admin = updateModel.adminId?.let {
                Worker.findById(it) ?: return@newSuspendedTransaction workerByIdNotFound(it).left()
            }
            GrantedRight.findById(id)?.apply {
                worker?.let { this.worker = it }
                admin?.let { this.admin = it }
                updateModel.code?.let { this.code = it }
            }?.let(mapper::invoke) ?: grantedRightByIdNotFound(id).left()
        }

    override suspend fun delete(id: GrantedRightId): Either<BackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            GrantedRight.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }
}