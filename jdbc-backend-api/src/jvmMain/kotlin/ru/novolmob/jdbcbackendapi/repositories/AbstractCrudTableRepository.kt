package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.backendapi.repositories.ICrudRepository
import ru.novolmob.jdbcbackendapi.utils.RepositoryUtil
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.tables.IdTable
import java.sql.ResultSet

abstract class AbstractCrudTableRepository<ID : Comparable<ID>, Model, CreateModel, UpdateModel>(
    open val table: IdTable<ID>,
    open val mapper: Mapper<ResultSet, Model>,
    open val notFoundException: (ID) -> AbstractBackendException
): ICrudRepository<ID, Model, CreateModel, UpdateModel> {
    override suspend fun get(id: ID): Either<AbstractBackendException, Model> =
        table.select(id) { fold(ifEmpty = { notFoundException(id) }, mapper::invoke) }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<Model>> =
        RepositoryUtil.getAll(table, pagination, mapper)

    override suspend fun delete(id: ID): Either<AbstractBackendException, Boolean> =
        (table.delete(id) > 0).right()

}