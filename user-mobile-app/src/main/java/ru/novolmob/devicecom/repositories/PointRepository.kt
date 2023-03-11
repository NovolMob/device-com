package ru.novolmob.devicecom.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Points
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.Page
import ru.novolmob.backendapi.models.PointFullModel
import ru.novolmob.backendapi.models.PointShortModel
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.devicecom.utils.KtorUtil.get

interface IPointRepository: IRepository {
    suspend fun getAll(): Either<AbstractBackendException, Page<PointShortModel>>
    suspend fun get(pointId: PointId): Either<AbstractBackendException, PointFullModel>
    suspend fun getAllByUserCity(): Either<AbstractBackendException, List<PointShortModel>>
}

class PointRepositoryImpl(
    private val client: HttpClient
): IPointRepository {
    override suspend fun getAll(): Either<AbstractBackendException, Page<PointShortModel>> =
        client.get(Points())

    override suspend fun get(pointId: PointId): Either<AbstractBackendException, PointFullModel> =
        client.get(Points.Id(points = Points(), id = pointId))

    override suspend fun getAllByUserCity(): Either<AbstractBackendException, List<PointShortModel>> =
        client.get(Points.ByCity(points = Points()))

}