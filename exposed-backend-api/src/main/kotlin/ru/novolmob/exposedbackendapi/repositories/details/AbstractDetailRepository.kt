package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
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
        Model, CreateModel, UpdateModel>(
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

    override suspend fun removeDetailFor(parentId: ParentID): Either<AbstractBackendException, Unit> =
        entityClass.deleteAllBy(parentId).right()

}