package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.core.models.Language

interface IDetailRepository<ID : Comparable<ID>, ParentID: Comparable<ParentID>, Model, CreateModel, UpdateModel>
    : ICrudRepository<ID, Model, CreateModel, UpdateModel> {
    suspend fun getDetailFor(parentId: ParentID, language: Language): Either<AbstractBackendException, Model>
    suspend fun getDetailsFor(parentId: ParentID): Either<AbstractBackendException, List<Model>>
    suspend fun removeDetailFor(parentId: ParentID): Either<AbstractBackendException, Unit>
}