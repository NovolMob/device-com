package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.GrantedRightCreateModel
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.backendapi.models.GrantedRightUpdateModel
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.ids.GrantedRightId
import ru.novolmob.core.models.ids.WorkerId

interface IGrantedRightRepository: ICrudRepository<GrantedRightId, GrantedRightModel, GrantedRightCreateModel, GrantedRightUpdateModel> {
    suspend fun getAllRightsFor(workerId: WorkerId): Either<BackendException, List<GrantedRightModel>>
    suspend fun contains(workerId: WorkerId, code: Code): Either<BackendException, Boolean>
    suspend fun removeFor(workerId: WorkerId, code: Code): Either<BackendException, Boolean>
}