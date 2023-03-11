package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.backendapi.repositories.ICrudRepository
import ru.novolmob.exposedbackendapi.utils.RepositoryUtil

abstract class AbstractCrudRepository<
        ID: Comparable<ID>,
        EntityClassT: EntityClass<ID, EntityT>,
        EntityT: Entity<ID>,
        Model, CreateModel, UpdateModel>(
    open val entityClass: EntityClassT,
    open val mapper: Mapper<EntityT, Model>,
    open val resultRowMapper: Mapper<ResultRow, Model>,
    open val notFoundException: (ID) -> AbstractBackendException,
): ICrudRepository<ID, Model, CreateModel, UpdateModel> {

    override suspend fun get(id: ID): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.findById(id)?.let(mapper::invoke) ?: notFoundException(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<Model>> =
        RepositoryUtil.generalGetAll(entityClass, pagination, resultRowMapper)

    override suspend fun delete(id: ID): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.findById(id)?.let {
                it.delete()
                true.right()
            } ?: false.right()
        }

    override suspend fun post(createModel: CreateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.new(createModel).flatMap(mapper::invoke)
        }

    override suspend fun post(id: ID, createModel: CreateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.findById(id)?.applyC(createModel)?.flatMap(mapper::invoke) ?: notFoundException(id).left()
        }

    override suspend fun put(id: ID, updateModel: UpdateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.findById(id)?.applyU(updateModel)?.flatMap(mapper::invoke) ?: notFoundException(id).left()
        }

    abstract fun EntityClassT.new(createModel: CreateModel): Either<AbstractBackendException, EntityT>
    abstract fun EntityT.applyC(createModel: CreateModel): Either<AbstractBackendException, EntityT>
    abstract fun EntityT.applyU(updateModel: UpdateModel): Either<AbstractBackendException, EntityT>

}