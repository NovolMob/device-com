package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.exposeddatabase.entities.details.CityDetail

class CityDetailMapper: Mapper<CityDetail, CityDetailModel> {
    override fun invoke(input: CityDetail): Either<AbstractBackendException, CityDetailModel> =
        CityDetailModel(
            id = input.id.value,
            parentId = input.parent.id.value,
            title = input.title,
            language = input.language
        ).right()
}