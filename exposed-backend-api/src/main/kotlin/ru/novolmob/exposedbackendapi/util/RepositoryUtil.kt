package ru.novolmob.exposedbackendapi.util

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.exposedbackendapi.mappers.Mapper

object RepositoryUtil {

    suspend fun <T, Table: org.jetbrains.exposed.sql.Table> generalGetAll(
        table: Table,
        pagination: Pagination,
        mapper: Mapper<ResultRow, T>,
        select: Table.() -> Query = { selectAll() }
    ): Either<AbstractBackendException, Page<T>> =
        newSuspendedTransaction(Dispatchers.IO) {
            val column = pagination.sortByColumn?.let { sortByColumn ->
                table.columns.singleOrNull { it.name == sortByColumn }
                    ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.tableDontHaveColumnException(
                        table,
                        sortByColumn
                    ).left()
            }
            val sortOrder = pagination.sortOrder?.let { sortOrder ->
                kotlin.runCatching { SortOrder.valueOf(sortOrder) }.getOrNull()
                    ?: return@newSuspendedTransaction ru.novolmob.exposedbackendapi.exceptions.badSortOrderException(
                        sortOrder
                    ).left()
            }
            val page = pagination.page ?: 0
            table
                .select()
                .let { query ->
                    column?.let { col ->
                        sortOrder?.let {
                            query.orderBy(col, it)
                        } ?: query.orderBy(col)
                    } ?: query
                }
                .let { query ->
                    pagination.pageSize?.let { size ->
                        query.limit(size.toInt(), size * page)
                    } ?: query
                }.parTraverseEither { mapper(it) }
                .flatMap {
                    Page(page = page, size = it.size.toLong(), it).right()
                }
        }

}