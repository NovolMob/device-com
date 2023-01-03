package ru.novolmob.user_mobile_app

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.novolmob.user_mobile_app.modules.*

class App: android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataStoreModule,
                clientModule,
                repositoryModule,
                serviceModule,
                viewModelModule
            )
        }
    }
}