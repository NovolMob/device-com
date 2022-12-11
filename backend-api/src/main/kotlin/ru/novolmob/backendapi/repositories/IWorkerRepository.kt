package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.WorkerCreateModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.models.WorkerUpdateModel
import ru.novolmob.database.models.Email
import ru.novolmob.database.models.Password
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.models.ids.WorkerId

interface IWorkerRepository: ICrudRepository<WorkerId, WorkerModel, WorkerCreateModel, WorkerUpdateModel> {
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<BackendException, WorkerModel>
    suspend fun login(email: Email, password: Password): Either<BackendException, WorkerModel>
}