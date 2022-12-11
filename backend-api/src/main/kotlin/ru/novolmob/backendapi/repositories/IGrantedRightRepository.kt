package ru.novolmob.backendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.GrantedRightModel
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.database.models.Code
import ru.novolmob.database.models.ids.UserId
import ru.novolmob.database.models.ids.WorkerId

interface IGrantedRightRepository: ICrudRepository<UserId, UserModel, UserCreateModel, UserUpdateModel> {
    suspend fun getAllRightsFor(workerId: WorkerId): Either<BackendException, List<GrantedRightModel>>
    suspend fun contains(workerId: WorkerId, code: Code): Either<BackendException, Boolean>
    suspend fun removeFor(workerId: WorkerId, code: Code): Either<BackendException, Boolean>
    suspend fun addFor(workerId: WorkerId, code: Code): Either<BackendException, Boolean>
}