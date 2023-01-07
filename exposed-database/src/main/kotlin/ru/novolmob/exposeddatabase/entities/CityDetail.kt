package ru.novolmob.exposeddatabase.entities

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.exposeddatabase.tables.CityDetails

class CityDetail(id: EntityID<CityDetailId>) : Entity<CityDetailId>(id) {
    companion object: EntityClass<CityDetailId, CityDetail>(CityDetails)

    var title by CityDetails.title
    var city by City referencedOn CityDetails.city
    var language by CityDetails.language
    var updateDate by CityDetails.updateDate
    var creationDate by CityDetails.creationDate
}