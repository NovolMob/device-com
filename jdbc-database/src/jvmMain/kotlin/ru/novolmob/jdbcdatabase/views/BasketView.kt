package ru.novolmob.jdbcdatabase.views

import ru.novolmob.jdbcdatabase.tables.Baskets

object BasketView: View() {
    val join = registerJoin(Baskets.deviceId, DetailView.DeviceDetailView.id)
    val userId = registerParameter(name = "user_id", reference = Baskets.userId)
    val deviceId = registerParameter(name = "device_id", reference = DetailView.DeviceDetailView.id)
    val title = registerParameter(reference = DetailView.DeviceDetailView.title)
    val description = registerParameter(reference = DetailView.DeviceDetailView.description)
    val amount = registerParameter(reference = DetailView.DeviceDetailView.amount)
    val price = registerParameter(reference = DetailView.DeviceDetailView.price)
    val language = registerParameter(reference = DetailView.DeviceDetailView.language)
    val amountInBasket = registerParameter(name = "amount_in_basket", reference =  Baskets.amount)
    val creationTime = registerParameter(reference = Baskets.creationTime)
}