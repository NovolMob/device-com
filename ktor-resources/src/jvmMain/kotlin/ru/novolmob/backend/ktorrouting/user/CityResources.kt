package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId

@Serializable
@Resource("/cities")
actual class Cities actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?,
    actual val language: Language
): Pagination {
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: CityId)
}