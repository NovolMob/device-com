package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.ids.WorkerId

fun workerByIdNotFound(workerId: WorkerId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Worker with id $workerId not found!"
    )