import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import modules.clientModule
import modules.repositoryModule
import modules.serviceModule
import modules.storageModule
import navigations.Navigation
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable
import org.koin.core.context.startKoin
import styles.Colors.generalBackground
import styles.Styles

fun main() {
    startKoin {
        modules(
            storageModule,
            clientModule,
            repositoryModule,
            serviceModule
        )
    }
    renderComposable(
        rootElementId = "root",
    ) {
        Style(Styles)
        val current by Navigation.current.collectAsState()

        Div(
            attrs = {
                style {
                    position(Position.Fixed)
                    top(0.px)
                    left(0.px)
                    width(100.percent)
                    height(100.percent)
                    backgroundColor(generalBackground)
                }
            }
        ) {
            current.screen()
        }
    }
}

