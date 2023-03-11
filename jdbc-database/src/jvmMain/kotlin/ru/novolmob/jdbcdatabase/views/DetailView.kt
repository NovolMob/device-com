package ru.novolmob.jdbcdatabase.views

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.*
import ru.novolmob.jdbcdatabase.tables.*
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.IParameter
import ru.novolmob.jdbcdatabase.tables.parameters.ViewParameter
import java.sql.ResultSet

sealed class DetailView<T: Any>(
    name: String? = null,
    val first: IParameter<T>,
    val second: IParameter<T>
): View(name) {
    val idJoin = registerJoin(first, second)
    abstract val language: ViewParameter<Language>
    suspend fun <R> select(id: T, language: Language, block: suspend ResultSet.() -> R): R = select(expression = (this.first eq id) and (this.language eq language), block = block)
    suspend fun <R> select(
        page: Int? = null, pageSize: Int? = null,
        sortByColumn: String? = null, sortOrder: String? = null,
        language: Language, block: suspend ResultSet.() -> R
    ): R =
        select(
            expression = (this.language eq language),
            limit = pageSize, offset = pageSize?.let { it * (page ?: 0) },
            orderBy = sortByColumn?.let { listOf(it + (sortOrder?.let { " $it" } ?: "")) },
            block = block
        )

    object CityDetailView: DetailView<CityId>(
        first = Cities.id,
        second = CityDetails.parentId
    ) {
        val id = registerParameter(reference = Cities.id)
        val title = registerParameter(reference = CityDetails.title)
        val detailId = registerParameter(name = "detail_id", CityDetails.id)
        override val language: ViewParameter<Language> = registerParameter(reference = CityDetails.language)
    }

    object PointDetailView: DetailView<PointId>(
        first = Points.id,
        second = PointDetails.parentId
    ) {
        val cityJoin = registerJoin(Points.cityId, Cities.id)
        val cityDetailJoin = registerJoin(Cities.id, CityDetails.parentId)

        val id = registerParameter(reference = Points.id)
        val detailId = registerParameter(name = "detail_id", PointDetails.id)
        val cityId = registerParameter(reference = Points.cityId)
        val cityDetailId = registerParameter(name = "city_detail_id", CityDetails.id)
        val cityTitle = registerParameter(name = "city_title", reference = CityDetails.title)
        val cityLanguage = registerParameter(name = "city_language", reference = CityDetails.language)
        val address = registerParameter(reference = PointDetails.address)
        val schedule = registerParameter(reference = PointDetails.schedule)
        val description = registerParameter(reference = PointDetails.description)
        override val language: ViewParameter<Language> = registerParameter(reference = PointDetails.language)

        suspend fun <T> select(
            cityId: CityId,
            language: Language,
            block: suspend ResultSet.() -> T
        ) = select(expression = (this.cityId eq cityId) and (this.language eq language), block = block)

    }

    object DeviceTypeDetailView: DetailView<DeviceTypeId>(
        first = DeviceTypes.id,
        second = DeviceTypeDetails.parentId
    ) {
        val id = registerParameter(reference = DeviceTypes.id)
        val detailId = registerParameter(name = "detail_id", DeviceTypeDetails.id)
        val title = registerParameter(reference = DeviceTypeDetails.title)
        val description = registerParameter(reference = DeviceTypeDetails.description)
        override val language: ViewParameter<Language> =
            registerParameter(reference = DeviceTypeDetails.language)
    }

    object DeviceDetailView: DetailView<DeviceId>(
        first = Devices.id,
        second = DeviceDetails.parentId
    ) {
        val typeJoin = registerJoin(Devices.typeId, DeviceTypes.id)
        val typeDetailJoin = registerJoin(DeviceTypes.id, DeviceTypeDetails.parentId)

        val id = registerParameter(reference = Devices.id)
        val detailId = registerParameter(name = "detail_id", DeviceDetails.id)
        val code = registerParameter(reference = Devices.code)

        val typeId = registerParameter(name = "type_id", reference = Devices.typeId)
        val typeDetailId = registerParameter(name = "type_detail_id", DeviceTypeDetails.id)
        val typeTitle = registerParameter(name = "type_title", reference = DeviceTypeDetails.title)
        val typeDescription = registerParameter(name = "type_description", reference = DeviceTypeDetails.description)
        val typeLanguage = registerParameter(name = "type_language", reference = DeviceTypeDetails.language)

        val amount = registerParameter(reference = Devices.amount)
        val price = registerParameter(reference = Devices.price)
        val title = registerParameter(reference = DeviceDetails.title)
        val description = registerParameter(reference = DeviceDetails.description)
        val features = registerParameter(reference = DeviceDetails.features)
        override val language: ViewParameter<Language> =
            registerParameter(reference = DeviceDetails.language)
    }

    object OrderStatusDetailView: DetailView<OrderStatusId>(
        first = OrderStatuses.id, second = OrderStatusDetails.parentId
    )  {
        val id = registerParameter(reference = OrderStatuses.id)
        val active = registerParameter(reference = OrderStatuses.active)
        val detailId = registerParameter(name = "detail_id", OrderStatusDetails.id)
        val title = registerParameter(reference = OrderStatusDetails.title)
        val description = registerParameter(reference = OrderStatusDetails.description)
        val dateTime = registerParameter(reference = OrderStatuses.creationTime)
        override val language: ViewParameter<Language> = registerParameter(reference = OrderStatusDetails.language)
    }

}