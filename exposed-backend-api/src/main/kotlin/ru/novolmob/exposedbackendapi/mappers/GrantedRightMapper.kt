package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.exposeddatabase.entities.GrantedRight

class GrantedRightMapper: Mapper<GrantedRight, GrantedRightModel> {
    override fun invoke(input: GrantedRight): Either<AbstractBackendException, GrantedRightModel> =
        GrantedRightModel(
            id = input.id.value,
            workerId = input.worker.id.value,
            code = input.code,
            adminId = input.admin.id.value
        ).right()
}