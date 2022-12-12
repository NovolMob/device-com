package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.Pagination

interface ICrudRepository<ID : Comparable<ID>, Model, CreateModel, UpdateModel>: IRepository {
    suspend fun get(id: ID): Either<BackendException, Model>
    suspend fun getAll(pagination: Pagination): Either<BackendException, Page<Model>>
    suspend fun post(createModel: CreateModel): Either<BackendException, Model>
    suspend fun post(id: ID, createModel: CreateModel): Either<BackendException, Model>
    suspend fun put(id: ID, updateModel: UpdateModel): Either<BackendException, Model>
    suspend fun delete(id: ID): Either<BackendException, Boolean>
}