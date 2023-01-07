package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposeddatabase.tables.Cities
import ru.novolmob.exposeddatabase.tables.CityDetails

class City(id: EntityID<CityId>) : Entity<CityId>(id) {
    companion object: EntityClass<CityId, City>(Cities)

    var creationDate by Cities.creationDate

    val details by CityDetail referrersOn CityDetails.city
}