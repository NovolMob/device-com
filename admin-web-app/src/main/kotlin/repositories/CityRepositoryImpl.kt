package repositories

import arrow.core.Either
import client.CustomHttpClient
import ru.novolmob.backend.ktorrouting.worker.Cities
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

interface ICityRepository: IRepository {
    suspend fun getDetail(id: CityDetailId): Either<AbstractBackendException, CityDetailModel>
    suspend fun postDetail(id: CityDetailId, createModel: CityDetailCreateModel): Either<AbstractBackendException, CityDetailModel>
    suspend fun putDetail(id: CityDetailId, updateModel: CityDetailUpdateModel): Either<AbstractBackendException, CityDetailModel>
    suspend fun getDetails(id: CityId): Either<AbstractBackendException, List<CityDetailModel>>
    suspend fun getAll(pagination: Cities): Either<AbstractBackendException, Page<CityModel>>
    suspend fun get(id: CityId): Either<AbstractBackendException, CityModel>
    suspend fun post(createModel: CityCreateModel): Either<AbstractBackendException, CityModel>
    suspend fun post(id: CityId, createModel: CityCreateModel): Either<AbstractBackendException, CityModel>
    suspend fun put(id: CityId, updateModel: CityUpdateModel): Either<AbstractBackendException, CityModel>
    suspend fun delete(id: CityId): Either<AbstractBackendException, Boolean>
    suspend fun addDetailFor(id: CityId, createModel: CityDetailCreateModel): Either<AbstractBackendException, CityModel>
}

class CityRepositoryImpl(
    private val client: CustomHttpClient
): ICityRepository {
    override suspend fun getDetail(id: CityDetailId): Either<AbstractBackendException, CityDetailModel> =
        client.get(Cities.Details(id))

    override suspend fun postDetail(
        id: CityDetailId,
        createModel: CityDetailCreateModel
    ): Either<AbstractBackendException, CityDetailModel> =
        client.post(Cities.Details(id), createModel)

    override suspend fun putDetail(
        id: CityDetailId,
        updateModel: CityDetailUpdateModel
    ): Either<AbstractBackendException, CityDetailModel> =
        client.put(Cities.Details(id), updateModel)

    override suspend fun getDetails(id: CityId): Either<AbstractBackendException, List<CityDetailModel>> =
        client.get(Cities.Id.Details(Cities.Id(id)))

    override suspend fun getAll(pagination: Cities): Either<AbstractBackendException, Page<CityModel>> =
        client.get(pagination)

    override suspend fun get(id: CityId): Either<AbstractBackendException, CityModel> =
        client.get(Cities.Id(id))

    override suspend fun post(createModel: CityCreateModel): Either<AbstractBackendException, CityModel> =
        client.post(Cities(), createModel)

    override suspend fun post(id: CityId, createModel: CityCreateModel): Either<AbstractBackendException, CityModel> =
        client.post(Cities.Id(id), createModel)

    override suspend fun put(id: CityId, updateModel: CityUpdateModel): Either<AbstractBackendException, CityModel> =
        client.put(Cities.Id(id), updateModel)

    override suspend fun delete(id: CityId): Either<AbstractBackendException, Boolean> =
        client.delete(Cities.Id(id))

    override suspend fun addDetailFor(
        id: CityId,
        createModel: CityDetailCreateModel
    ): Either<AbstractBackendException, CityModel> =
        client.post(Cities.Id.Details(Cities.Id(id)), createModel)

}