package ru.novolmob.backendapi.mappers

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException

interface Mapper<Input, Output> {
    operator fun invoke(input: Input): Either<AbstractBackendException, Output>
}