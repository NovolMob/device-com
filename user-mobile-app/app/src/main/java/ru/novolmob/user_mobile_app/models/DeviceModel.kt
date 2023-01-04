package ru.novolmob.user_mobile_app.models

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.novolmob.core.models.ids.DeviceId

class DeviceModel(
    val id: DeviceId,
    imageBitmap: ImageBitmap? = null,
    val title: String,
    val description: String,
    val price: Double,
    amountInBasket: Int
) {
    val priceString: String
        get() = "$price ₽"

    private val _imageFlow = MutableStateFlow(imageBitmap)
    val imageFlow = _imageFlow.asStateFlow()

    private val _amountInBasketFlow = MutableStateFlow(amountInBasket)
    val amountInBasketFlow = _amountInBasketFlow.asStateFlow()


    suspend fun image(imageBitmap: ImageBitmap?) = _imageFlow.emit(imageBitmap)
    suspend fun amountInBasket(value: Int) = _amountInBasketFlow.emit(value)
}