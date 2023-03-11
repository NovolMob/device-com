package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.ids.BasketId

fun basketByIdNotFoundException(id: BasketId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Basket row with id $id not found!"
    )

