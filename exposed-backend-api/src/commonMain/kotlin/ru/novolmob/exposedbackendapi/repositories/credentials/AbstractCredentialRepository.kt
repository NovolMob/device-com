package ru.novolmob.exposedbackendapi.repositories.credentials

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.repositories.ICredentialRepository
import ru.novolmob.exposeddatabase.entities.credentials.CredentialEntity
import ru.novolmob.exposeddatabase.entities.credentials.CredentialEntityClass

abstract class AbstractCredentialRepository<
        EntityClassT: CredentialEntityClass<EntityT, Parent, ParentID>,
        EntityT: CredentialEntity<Parent, ParentID>,
        Parent: Entity<ParentID>,
        ParentID: Comparable<ParentID>,
        Model, UpdateModel>(
    open val entityClass: EntityClassT,
    open val mapper: Mapper<EntityT, Model>,
    open val notFoundByParentIdException: (ParentID) -> AbstractBackendException
): ICredentialRepository<ParentID, Model, UpdateModel> {
    override suspend fun getBy(parentId: ParentID): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.findBy(parentId)?.let(mapper::invoke) ?: notFoundByParentIdException(parentId).left()
        }

    override suspend fun post(createModel: Model): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.new(createModel).flatMap(mapper::invoke)
        }

    override suspend fun put(parentId: ParentID, updateModel: UpdateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction(Dispatchers.IO) {
            entityClass.findBy(parentId)?.apply(updateModel)?.flatMap(mapper::invoke) ?: notFoundByParentIdException(parentId).left()
        }

    abstract suspend fun EntityClassT.new(createModel: Model): Either<AbstractBackendException, EntityT>
    abstract suspend fun EntityT.apply(updateModel: UpdateModel): Either<AbstractBackendException, EntityT>

}