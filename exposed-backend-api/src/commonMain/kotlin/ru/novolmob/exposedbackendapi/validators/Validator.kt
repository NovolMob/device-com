package ru.novolmob.exposedbackendapi.validators

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException

interface Validator<Target> {
    operator fun invoke(target: Target): Either<AbstractBackendException, Target>
}