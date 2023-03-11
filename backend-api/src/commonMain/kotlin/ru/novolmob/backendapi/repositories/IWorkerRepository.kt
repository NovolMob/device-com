package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.WorkerCreateModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.models.WorkerUpdateModel
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId

interface IWorkerRepository: ICrudRepository<WorkerId, WorkerModel, WorkerCreateModel, WorkerUpdateModel> {
    suspend fun getAllByPointId(pointId: PointId): Either<AbstractBackendException, List<WorkerModel>>
    suspend fun login(phoneNumber: PhoneNumber, password: Password): Either<AbstractBackendException, WorkerModel>
    suspend fun login(email: Email, password: Password): Either<AbstractBackendException, WorkerModel>
}