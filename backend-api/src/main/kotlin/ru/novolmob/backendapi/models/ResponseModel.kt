package ru.novolmob.backendapi.models

import ru.novolmob.backendapi.exceptions.BackendException

data class ResponseModel<T>(
    val data: T?,
    val exception: BackendException?
)