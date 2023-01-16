package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.cityByIdNotFound
import ru.novolmob.backendapi.exceptions.cityDetailByCityIdAndLanguageNotFound
import ru.novolmob.backendapi.exceptions.cityDetailByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityDetailCreateModel
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.models.CityDetailUpdateModel
import ru.novolmob.backendapi.repositories.ICityDetailRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposeddatabase.entities.City
import ru.novolmob.exposeddatabase.entities.details.CityDetail

class CityDetailRepositoryImpl(
    mapper: Mapper<CityDetail, CityDetailModel>,
    resultRowMapper: Mapper<ResultRow, CityDetailModel>,
): ICityDetailRepository,
    AbstractDetailRepository<CityDetailId, CityDetail.Companion, CityDetail, City, CityId, CityDetailModel, CityDetailCreateModel, CityDetailUpdateModel>(
        CityDetail.Companion, mapper, resultRowMapper, ::cityDetailByIdNotFound, ::cityDetailByCityIdAndLanguageNotFound
    ) {

    override fun CityDetail.Companion.new(createModel: CityDetailCreateModel): Either<AbstractBackendException, CityDetail> {
        val city = City.findById(createModel.parentId) ?: return cityByIdNotFound(createModel.parentId).left()
        return new {
            this.parent = city
            this.title = createModel.title
            this.language = createModel.language
        }.right()
    }

    override fun CityDetail.applyC(createModel: CityDetailCreateModel): Either<AbstractBackendException, CityDetail> {
        val city = City.findById(createModel.parentId) ?: return cityByIdNotFound(createModel.parentId).left()
        return CityDetail.new {
            this.parent = city
            this.title = createModel.title
            this.language = createModel.language
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun CityDetail.applyU(updateModel: CityDetailUpdateModel): Either<AbstractBackendException, CityDetail> {
        val city = updateModel.parentId?.let {
            City.findById(it) ?: return cityByIdNotFound(it).left()
        }
        return apply {
            city?.let { this.parent = it }
            updateModel.title?.let { this.title = it }
            updateModel.language?.let { this.language = it }
            this.updateDate = UpdateTime.now()
        }.right()
    }
}