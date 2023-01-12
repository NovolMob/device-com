package ru.novolmob.jdbcdatabase.views

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.tables.Orders
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import java.sql.ResultSet

object OrderView: View() {
    val pointJoin = registerJoin(Orders.pointId, DetailView.PointDetailView.id)

    val id = registerParameter(reference = Orders.id)
    val userId = registerParameter(reference = Orders.userId)
    val pointId = registerParameter(reference = Orders.pointId)
    val cityId = registerParameter(reference = DetailView.PointDetailView.cityId)
    val cityDetailId = registerParameter(name = "city_detail_id", reference = DetailView.PointDetailView.cityDetailId)
    val cityTitle = registerParameter(name = "city_title", reference = DetailView.PointDetailView.cityTitle)
    val cityLanguage = registerParameter(name = "city_language", reference = DetailView.PointDetailView.cityLanguage)
    val pointDetailId = registerParameter(name = "point_detail_id", reference = DetailView.PointDetailView.detailId)
    val pointAddress = registerParameter(name = "point_address", reference = DetailView.PointDetailView.address)
    val pointSchedule = registerParameter(name = "point_schedule", reference = DetailView.PointDetailView.schedule)
    val pointDescription = registerParameter(name = "point_description", reference = DetailView.PointDetailView.description)
    val pointLanguage = registerParameter(name = "point_language", reference = DetailView.PointDetailView.language)
    val totalCost = registerParameter(reference = Orders.totalCost)
    val creationTime = registerParameter(reference = Orders.creationTime)

    suspend fun <T> select(
        orderId: OrderId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T =
        select(
            expression = (this.id eq orderId) and (this.cityLanguage eq language) and (this.pointLanguage eq language),
            block = block
        )

    suspend fun <T> select(
        userId: UserId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T =
        select(
            expression = (this.userId eq userId) and (this.cityLanguage eq language) and (this.pointLanguage eq language),
            block = block
        )

}