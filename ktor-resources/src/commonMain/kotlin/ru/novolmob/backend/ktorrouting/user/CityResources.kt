package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId

@Serializable
@Resource("cities")
class Cities(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null,
    val language: Language
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val id: CityId, val cities: Cities)
}