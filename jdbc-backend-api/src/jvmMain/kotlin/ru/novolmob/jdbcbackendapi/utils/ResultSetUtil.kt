package ru.novolmob.jdbcbackendapi.utils

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverseEither
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.Page
import java.sql.ResultSet

object ResultSetUtil {

    suspend fun <M> Either<AbstractBackendException, ResultSet>.list(mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, List<M>> =
        flatMap { it.list(mapper) }

    suspend fun <M> ResultSet.list(mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, List<M>> {
        val list = mutableListOf<Either<AbstractBackendException, M>>()
        while (next()) {
            list.add(mapper(this))
        }
        return list.parTraverseEither { it }
    }

    suspend fun <M> ResultSet.pagination(page: Long, mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, Page<M>> =
        list(mapper).flatMap {
            Page(page, it.size.toLong(), list = it).right()
        }

    suspend fun <M> Either<AbstractBackendException, ResultSet>.pagination(page: Long, mapper: Mapper<ResultSet, M>): Either<AbstractBackendException, Page<M>> =
        flatMap { it.pagination(page, mapper) }

    fun <R> Either<AbstractBackendException, ResultSet>.fold(ifEmpty: () -> AbstractBackendException, block: (ResultSet) -> Either<AbstractBackendException, R>): Either<AbstractBackendException, R> =
        flatMap { fold(ifEmpty, block) }

    fun <R> ResultSet.fold(ifEmpty: () -> AbstractBackendException, block: (ResultSet) -> Either<AbstractBackendException, R>): Either<AbstractBackendException, R> {
        if (!next()) return ifEmpty().left()
        return block(this)
    }

}