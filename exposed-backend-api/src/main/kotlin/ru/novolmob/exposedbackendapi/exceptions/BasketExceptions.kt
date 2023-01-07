package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.ids.BasketId

fun basketByIdNotFoundException(id: BasketId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Basket row with id $id not found!"
    )

