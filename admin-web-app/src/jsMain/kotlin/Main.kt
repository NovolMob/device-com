import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import modules.clientModule
import modules.repositoryModule
import modules.serviceModule
import modules.storageModule
import naavigations.Navigation
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.koin.core.context.startKoin
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
        current.screen()
    }
}

