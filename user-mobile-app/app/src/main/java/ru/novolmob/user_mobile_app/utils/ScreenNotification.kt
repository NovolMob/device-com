package ru.novolmob.user_mobile_app.utils

import androidx.compose.ui.graphics.Color
import arrow.core.Either
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.novolmob.backendapi.exceptions.BackendException

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

        private fun BackendException.toScreenNotification() =
            ScreenNotification(backgroundColor = Color.Red, message = message)

        suspend fun push(exception: BackendException, duration: Long = DEFAULT_DURATION) =
            push(notification = exception.toScreenNotification(), duration = duration)

        suspend fun push(notification: ScreenNotification, duration: Long = DEFAULT_DURATION) {
            _notification.update { notification }
            delay(duration)
            _notification.update { it?.copy(visible = false) }
        }

        suspend fun push(
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

        suspend fun <M> Either<BackendException, M>.notice(duration: Long = DEFAULT_DURATION) =
            fold(
                ifLeft = { push(it) },
                ifRight = {}
            )

    }

}