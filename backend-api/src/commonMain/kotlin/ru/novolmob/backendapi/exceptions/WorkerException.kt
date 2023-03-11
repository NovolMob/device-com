package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.ids.WorkerId

fun workerByIdNotFound(workerId: WorkerId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Worker with id $workerId not found!"
    )

fun failedToCreateWorker() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create worker!"
    )