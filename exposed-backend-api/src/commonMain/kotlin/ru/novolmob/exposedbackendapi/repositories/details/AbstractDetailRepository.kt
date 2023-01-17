package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.detailWithParentIDAndLanguageIsExists
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DetailCreate
import ru.novolmob.backendapi.models.DetailUpdate
import ru.novolmob.backendapi.repositories.IDetailRepository
import ru.novolmob.core.models.Language
import ru.novolmob.exposedbackendapi.repositories.AbstractCrudRepository
import ru.novolmob.exposeddatabase.entities.details.DetailEntity
import ru.novolmob.exposeddatabase.entities.details.DetailEntityClass

abstract class AbstractDetailRepository<
        ID : Comparable<ID>,
        EntityClassT: DetailEntityClass<ID, EntityT, Parent, ParentID>,
        EntityT: DetailEntity<ID, Parent, ParentID>,
        Parent: Entity<ParentID>,
        ParentID: Comparable<ParentID>,
        Model, CreateModel: DetailCreate<ParentID>, UpdateModel: DetailUpdate<ParentID>>(
    override val entityClass: EntityClassT,
    mapper: Mapper<EntityT, Model>,
    resultRowMapper: Mapper<ResultRow, Model>,
    notFoundException: (ID) -> AbstractBackendException,
    open val notFoundByParentIdException: (ParentID, Language) -> AbstractBackendException
): IDetailRepository<ID, ParentID, Model, CreateModel, UpdateModel>,
    AbstractCrudRepository<ID, EntityClassT, EntityT, Model, CreateModel, UpdateModel>(
        entityClass, mapper, resultRowMapper, notFoundException
    ) {

    override suspend fun getDetailFor(
        parentId: ParentID,
        language: Language
    ): Either<AbstractBackendException, Model> =
        entityClass.findBy(parentId, language)?.let(mapper::invoke) ?: notFoundByParentIdException(parentId, language).left()

    override suspend fun getDetailsFor(parentId: ParentID): Either<AbstractBackendException, List<Model>> =
        entityClass.allBy(parentId).parTraverseEither{ mapper(it) }

    override suspend fun removeDetailsFor(parentId: ParentID): Either<AbstractBackendException, Unit> =
        entityClass.deleteAllBy(parentId).right()

    override suspend fun post(createModel: CreateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction {
            entityClass.findBy(createModel.parentId, createModel.language)?.let {
                super.post(it.id.value, createModel)
            } ?: super.post(createModel)
        }

    override suspend fun post(id: ID, createModel: CreateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction {
            entityClass.findBy(createModel.parentId, createModel.language)?.let {
                if (it.id.value != id) {
                    return@newSuspendedTransaction detailWithParentIDAndLanguageIsExists(createModel.parentId, createModel.language).left()
                }
            }
            super.post(id, createModel)
        }

    override suspend fun put(id: ID, updateModel: UpdateModel): Either<AbstractBackendException, Model> =
        newSuspendedTransaction {
            entityClass.findById(id)?.let {
                val parentId = updateModel.parentId ?: it.parent.id.value
                val language = updateModel.language ?: it.language
                entityClass.findBy(parentId, language)?.let {
                    if (it.id.value != id) {
                        return@newSuspendedTransaction detailWithParentIDAndLanguageIsExists(parentId, language).left()
                    }
                }
                it.applyU(updateModel)
            }?.flatMap(mapper::invoke) ?: notFoundException(id).left()
        }

}