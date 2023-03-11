package ru.novolmob.jdbcbackendapi.repositories.details

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.DetailCreate
import ru.novolmob.backendapi.models.DetailUpdate
import ru.novolmob.backendapi.repositories.IDetailRepository
import ru.novolmob.core.models.Language
import ru.novolmob.jdbcbackendapi.repositories.AbstractCrudTableRepository
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.tables.DetailTable
import java.sql.ResultSet

abstract class AbstractDetailRepository<ID : Comparable<ID>, ParentID: Comparable<ParentID>, Model, CreateModel: DetailCreate<ParentID>, UpdateModel: DetailUpdate<ParentID>>(
    override val table: DetailTable<ID, ParentID>,
    mapper: Mapper<ResultSet, Model>,
    notFoundException: (ID) -> AbstractBackendException,
    open val notFoundByParentIdException: (ParentID, Language) -> AbstractBackendException
): IDetailRepository<ID, ParentID, Model, CreateModel, UpdateModel>, AbstractCrudTableRepository<ID, Model, CreateModel, UpdateModel>(
    table, mapper, notFoundException
) {
    override suspend fun getDetailFor(parentId: ParentID, language: Language): Either<AbstractBackendException, Model> =
        table.selectByParentIdAndLanguage(parentId, language) { fold(ifEmpty = { notFoundByParentIdException(parentId, language) }, mapper::invoke) }

    override suspend fun removeDetailsFor(parentId: ParentID): Either<AbstractBackendException, Unit> {
        table.deleteByParentId(parentId)
        return Unit.right()
    }

    override suspend fun getDetailsFor(parentId: ParentID): Either<AbstractBackendException, List<Model>> =
        table.selectByParentId(parentId) { list(mapper) }
}