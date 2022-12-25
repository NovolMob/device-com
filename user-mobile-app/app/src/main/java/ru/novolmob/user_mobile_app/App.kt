package ru.novolmob.user_mobile_app

import org.koin.core.context.startKoin
import ru.novolmob.user_mobile_app.modules.serviceModule
import ru.novolmob.user_mobile_app.modules.viewModelModule

class App: android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                serviceModule,
                viewModelModule
            )
        }
    }
}