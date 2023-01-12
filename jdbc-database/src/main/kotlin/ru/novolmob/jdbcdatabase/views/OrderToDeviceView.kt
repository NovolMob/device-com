package ru.novolmob.jdbcdatabase.views

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcdatabase.tables.OrderToDeviceTable
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import java.sql.ResultSet

object OrderToDeviceView: View() {

    val join = registerJoin(OrderToDeviceTable.deviceId, DetailView.DeviceDetailView.id)

    val orderId = registerParameter(reference = OrderToDeviceTable.orderId)
    val deviceId = registerParameter(reference = OrderToDeviceTable.deviceId)
    val amount = registerParameter(reference = OrderToDeviceTable.amount)
    val priceForOne = registerParameter(reference = OrderToDeviceTable.priceForOne)
    val deviceDetailId = registerParameter(name = "order_status_detail_id", reference = DetailView.DeviceDetailView.detailId)
    val deviceTitle = registerParameter(name = "order_status_title", reference = DetailView.DeviceDetailView.title)
    val deviceDescription = registerParameter(name = "order_status_description", reference = DetailView.DeviceDetailView.description)
    val deviceLanguage = registerParameter(name = "order_status_language", reference = DetailView.DeviceDetailView.language)

    suspend fun <T> select(
        orderId: OrderId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T = select(
        expression = (this.orderId eq orderId) and (this.deviceLanguage eq language),
        block = block
    )

}