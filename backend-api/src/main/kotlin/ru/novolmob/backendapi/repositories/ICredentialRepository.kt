package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException

interface ICredentialRepository<ParentID: Comparable<ParentID>, Model, UpdateModel>: IRepository {
    suspend fun getBy(parentId: ParentID): Either<AbstractBackendException, Model>
    suspend fun post(createModel: Model): Either<AbstractBackendException, Model>
    suspend fun put(parentId: ParentID, updateModel: UpdateModel): Either<AbstractBackendException, Model>
}