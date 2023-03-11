package ru.novolmob.jdbcbackendapi.utils

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.pagination
import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.views.DetailView
import ru.novolmob.jdbcdatabase.views.View
import java.sql.ResultSet

object RepositoryUtil {

    suspend fun <M> getAll(table: Table, pagination: Pagination, mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, Page<M>> =
        table.select(
            page = pagination.page?.toInt(), pageSize = pagination.pageSize?.toInt(),
            sortByColumn = pagination.sortByColumn, pagination.sortOrder
        ) { pagination(pagination.page ?: 0, mapper) }

    suspend fun <M> getAll(view: View, pagination: Pagination, mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, Page<M>> =
        view.select(
            page = pagination.page?.toInt(), pageSize = pagination.pageSize?.toInt(),
            sortByColumn = pagination.sortByColumn, pagination.sortOrder
        ) { pagination(pagination.page ?: 0, mapper) }

    suspend fun <M> getAll(view: DetailView<*>, pagination: Pagination, language: Language, mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, Page<M>> =
        view.select(
            page = pagination.page?.toInt(), pageSize = pagination.pageSize?.toInt(),
            sortByColumn = pagination.sortByColumn, pagination.sortOrder, language = language
        ) { pagination(pagination.page ?: 0, mapper) }

}