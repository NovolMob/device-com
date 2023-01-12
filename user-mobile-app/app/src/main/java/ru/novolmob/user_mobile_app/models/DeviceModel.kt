package ru.novolmob.user_mobile_app.models

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.novolmob.core.models.ids.DeviceId

data class DeviceModel(
    val id: DeviceId,
    var title: String,
    var description: String,
    var price: Double
) {
    val priceString: String
        get() = "$price ₽"

    private val _imageFlow = MutableStateFlow<ImageBitmap?>(null)
    val imageFlow = _imageFlow.asStateFlow()

    private val _amountInBasketFlow = MutableStateFlow(0)
    val amountInBasketFlow = _amountInBasketFlow.asStateFlow()


    suspend fun image(imageBitmap: ImageBitmap?) = _imageFlow.emit(imageBitmap)
    suspend fun amountInBasket(value: Int) = _amountInBasketFlow.emit(value)
}