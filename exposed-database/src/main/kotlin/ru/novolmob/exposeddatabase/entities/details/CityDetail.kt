package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposeddatabase.entities.City
import ru.novolmob.exposeddatabase.tables.details.CityDetails

class CityDetail(id: EntityID<CityDetailId>) : DetailEntity<CityDetailId, City, CityId>(id) {
    companion object: DetailEntityClass<CityDetailId, CityDetail, City, CityId>(CityDetails)

    var title by CityDetails.title
    override var parent by City referencedOn CityDetails.parent
    override var language by CityDetails.language
    var updateDate by CityDetails.updateDate
    var creationDate by CityDetails.creationDate
}