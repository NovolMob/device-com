package ru.novolmob.backend.exceptions

import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.ids.BasketId

fun basketByIdNotFoundException(id: BasketId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Basket row with id $id not found!"
    )

