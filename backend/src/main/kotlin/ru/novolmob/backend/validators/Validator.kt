package ru.novolmob.backend.validators

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException

interface Validator<Target> {
    operator fun invoke(target: Target): Either<BackendException, Target>
}