package ru.novolmob.backend.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException

interface Mapper<Input, Output> {
    operator fun invoke(input: Input): Either<BackendException, Output>
}