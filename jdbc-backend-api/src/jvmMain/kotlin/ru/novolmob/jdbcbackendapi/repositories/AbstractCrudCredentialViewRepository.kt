package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.backendapi.repositories.ICrudRepository
import ru.novolmob.core.models.ids.UUIDable
import ru.novolmob.jdbcbackendapi.utils.RepositoryUtil
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcdatabase.views.CredentialView
import java.sql.ResultSet

abstract class AbstractCrudCredentialViewRepository<ID, Model, CreateModel, UpdateModel>(
    open val view: CredentialView<ID>,
    open val mapper: Mapper<ResultSet, Model>,
    open val notFoundException: (ID) -> AbstractBackendException
): ICrudRepository<ID, Model, CreateModel, UpdateModel> where ID : UUIDable, ID : Comparable<ID> {
    override suspend fun get(id: ID): Either<AbstractBackendException, Model> =
        view.select(id) { fold(ifEmpty = { notFoundException(id) }, mapper::invoke) }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<Model>> =
        RepositoryUtil.getAll(view, pagination, mapper)

}