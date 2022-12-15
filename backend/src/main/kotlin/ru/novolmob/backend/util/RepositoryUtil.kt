package ru.novolmob.backend.util

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backend.exceptions.badSortOrderException
import ru.novolmob.backend.exceptions.tableDontHaveColumnException
import ru.novolmob.backend.mappers.Mapper
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination

object RepositoryUtil {

    suspend fun <T> generalGatAll(table: Table, pagination: Pagination, mapper: Mapper<ResultRow, T>): Either<BackendException, Page<T>> =
        newSuspendedTransaction(Dispatchers.IO) {
            val column = pagination.sortByColumn?.let { sortByColumn ->
                table.columns.singleOrNull { it.name == sortByColumn }
                    ?: return@newSuspendedTransaction tableDontHaveColumnException(table, sortByColumn).left()
            }
            val sortOrder = pagination.sortOrder?.let { sortOrder ->
                kotlin.runCatching { SortOrder.valueOf(sortOrder) }.getOrNull()
                    ?: return@newSuspendedTransaction badSortOrderException(sortOrder).left()
            }
            val page = pagination.page ?: 0
            table
                .selectAll()
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