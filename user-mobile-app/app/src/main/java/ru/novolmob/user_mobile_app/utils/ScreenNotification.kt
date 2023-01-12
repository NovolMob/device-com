package ru.novolmob.user_mobile_app.utils

import androidx.compose.ui.graphics.Color
import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException

data class ScreenNotification(
    val backgroundColor: Color = DEFAULT_COLOR,
    val textColor: Color = DEFAULT_TEXT_COLOR,
    val message: String = "",
    val visible: Boolean = true
) {

    companion object {
        private val _notification = MutableStateFlow<ScreenNotification?>(null)
        val notification = _notification.asStateFlow()

        private const val DEFAULT_DURATION = 4000L
        private val DEFAULT_COLOR = Color(213, 213, 213, 255)
        private val DEFAULT_TEXT_COLOR = Color.Black

        private fun AbstractBackendException.toScreenNotification() =
            ScreenNotification(backgroundColor = Color.Red, message = message)

        fun push(exception: AbstractBackendException, duration: Long = DEFAULT_DURATION) =
            push(notification = exception.toScreenNotification(), duration = duration)

        fun push(notification: ScreenNotification, duration: Long = DEFAULT_DURATION) {
            CoroutineScope(Dispatchers.Default).launch {
                _notification.update { notification }
                delay(duration)
                _notification.update { it?.copy(visible = false) }
            }
        }

        fun push(
            message: String,
            color: Color = DEFAULT_COLOR,
            textColor: Color = DEFAULT_TEXT_COLOR,
            duration: Long = DEFAULT_DURATION
        ) = push(
            notification = ScreenNotification(
                backgroundColor = color, textColor = textColor, message = message
            ),
            duration = duration
        )

        fun <M> Either<AbstractBackendException, M>.notice(duration: Long = DEFAULT_DURATION): M? =
            fold(
                ifLeft = {
                    push(it, duration)
                    null
                },
                ifRight = { it }
            )

    }

}