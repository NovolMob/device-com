package ru.novolmob.jdbcdatabase.views

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcdatabase.tables.OrderToStatusTable
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import java.sql.ResultSet

object OrderToStatusView: View() {

    val join = registerJoin(OrderToStatusTable.orderStatusId, DetailView.OrderStatusDetailView.id)

    val orderId = registerParameter(reference = OrderToStatusTable.orderId)
    val orderStatusId = registerParameter(reference = OrderToStatusTable.orderStatusId)
    val orderStatusActive = registerParameter(name = "order_status_active", reference = DetailView.OrderStatusDetailView.active)
    val orderStatusDetailId = registerParameter(name = "order_status_detail_id", reference = DetailView.OrderStatusDetailView.detailId)
    val orderStatusTitle = registerParameter(name = "order_status_title", reference = DetailView.OrderStatusDetailView.title)
    val orderStatusDescription = registerParameter(name = "order_status_description", reference = DetailView.OrderStatusDetailView.description)
    val orderStatusLanguage = registerParameter(name = "order_status_language", reference = DetailView.OrderStatusDetailView.language)
    val workerId = registerParameter(name = "worker_id", reference = OrderToStatusTable.workerId)
    val creationTime = registerParameter(reference = OrderToStatusTable.creationTime)

    suspend fun <T> select(
        orderId: OrderId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T = select(
        expression = (this.orderId eq orderId) and (this.orderStatusLanguage eq language),
        block = block
    )

}