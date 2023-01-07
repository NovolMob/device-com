package ru.novolmob.exposedbackendapi.repositories

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.ICityDetailRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposedbackendapi.exceptions.cityByIdNotFound
import ru.novolmob.exposedbackendapi.exceptions.cityDetailByCityIdAndLanguageNotFound
import ru.novolmob.exposedbackendapi.exceptions.cityDetailByIdNotFound
import ru.novolmob.exposedbackendapi.mappers.Mapper
import ru.novolmob.exposedbackendapi.util.RepositoryUtil
import ru.novolmob.exposeddatabase.entities.City
import ru.novolmob.exposeddatabase.entities.CityDetail
import ru.novolmob.exposeddatabase.tables.CityDetails

class CityDetailRepositoryImpl(
    val mapper: Mapper<CityDetail, CityDetailModel>,
    val resultRowMapper: Mapper<ResultRow, CityDetailModel>,
): ICityDetailRepository {
    override suspend fun getDetailFor(
        cityId: CityId,
        language: Language
    ): Either<AbstractBackendException, CityDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            CityDetail.find { (CityDetails.city eq cityId) and (CityDetails.language eq language) }
                .limit(1).firstOrNull()?.let(mapper::invoke) ?: cityDetailByCityIdAndLanguageNotFound(cityId, language).left()
        }

    override suspend fun removeFor(cityId: CityId): Either<AbstractBackendException, Boolean> =
        newSuspendedTransaction(Dispatchers.IO) {
            CityDetail.find { CityDetails.city eq cityId }
                .takeIf { !it.empty() }?.let {
                    it.parTraverse { it.delete() }
                    true.right()
                } ?: false.right()
        }

    override suspend fun get(id: CityDetailId): Either<AbstractBackendException, CityDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            CityDetail.findById(id)?.let(mapper::invoke) ?: cityDetailByIdNotFound(id).left()
        }

    override suspend fun getAll(pagination: Pagination): Either<AbstractBackendException, Page<CityDetailModel>> =
        RepositoryUtil.generalGetAll(CityDetails, pagination, resultRowMapper)

    override suspend fun post(createModel: CityDetailCreateModel): Either<AbstractBackendException, CityDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val city = City.findById(createModel.cityId) ?: return@newSuspendedTransaction cityByIdNotFound(createModel.cityId).left()
            CityDetail.new {
                this.city = city
                this.title = createModel.title
                this.language = createModel.language
            }.let(mapper::invoke)
        }

    override suspend fun post(
        id: CityDetailId,
        createModel: CityDetailCreateModel
    ): Either<AbstractBackendException, CityDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val city = City.findById(createModel.cityId) ?: return@newSuspendedTransaction cityByIdNotFound(createModel.cityId).left()
            CityDetail.findById(id)?.apply {
                this.city = city
                this.title = createModel.title
                this.language = createModel.language
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: cityDetailByIdNotFound(id).left()
        }

    override suspend fun put(
        id: CityDetailId,
        updateModel: CityDetailUpdateModel
    ): Either<AbstractBackendException, CityDetailModel> =
        newSuspendedTransaction(Dispatchers.IO) {
            val city = updateModel.cityId?.let {
                City.findById(it) ?: return@newSuspendedTransaction cityByIdNotFound(it).left()
            }
            CityDetail.findById(id)?.apply {
                city?.let { this.city = it }
                updateModel.title?.let { this.title = it }
                updateModel.language?.let { this.language = it }
                this.updateDate = UpdateTime.now()
            }?.let(mapper::invoke) ?: cityDetailByIdNotFound(id).left()
        }

    override suspend fun delete(id: CityDetailId): Either<AbstractBackendException, Boolean> =
        CityDetail.findById(id)?.let {
            it.delete()
            true.right()
        } ?: false.right()
}