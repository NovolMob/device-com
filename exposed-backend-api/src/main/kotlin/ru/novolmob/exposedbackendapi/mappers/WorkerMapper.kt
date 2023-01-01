package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.exposedbackendapi.exceptions.workerCredentialByWorkerIdNotFoundException
import ru.novolmob.exposeddatabase.entities.Worker

class WorkerMapper: Mapper<Worker, WorkerModel> {
    override fun invoke(input: Worker): Either<BackendException, WorkerModel> =
        input.credentials.firstOrNull()?.let {
            WorkerModel(
                id = input.id.value,
                pointId = input.point?.id?.value,
                firstname = input.firstname,
                lastname = input.lastname,
                patronymic = input.patronymic,
                language = input.language,
                phoneNumber = it.phoneNumber,
                email = it.email
            ).right()
        } ?: workerCredentialByWorkerIdNotFoundException(input.id.value).left()
}