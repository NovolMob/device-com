package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination

interface ICrudRepository<ID : Comparable<ID>, Model, CreateModel, UpdateModel>: IRepository {
    suspend fun get(id: ID): Either<AbstractBackendException, Model>
    suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<Model>>
    suspend fun post(createModel: CreateModel): Either<AbstractBackendException, Model>
    suspend fun post(id: ID, createModel: CreateModel): Either<AbstractBackendException, Model>
    suspend fun put(id: ID, updateModel: UpdateModel): Either<AbstractBackendException, Model>
    suspend fun delete(id: ID): Either<AbstractBackendException, Boolean>
}