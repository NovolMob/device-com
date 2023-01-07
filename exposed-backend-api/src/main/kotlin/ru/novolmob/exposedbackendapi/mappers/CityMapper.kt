package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityModel
import ru.novolmob.exposeddatabase.entities.City

class CityMapper: Mapper<City, CityModel> {
    override fun invoke(input: City): Either<AbstractBackendException, CityModel> =
        CityModel(
            id = input.id.value
        ).right()
}