package ru.novolmob.user_mobile_app.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ru.novolmob.user_mobile_app.services.IService

object ServiceUtil: KoinComponent {

    fun clearAllServices() {
        CoroutineScope(Dispatchers.IO).launch {
            val services: List<IService> = getKoin().getAll()
            services.forEach { it.clear() }
        }
    }

    fun updateAllServices() {
        CoroutineScope(Dispatchers.IO).launch {
            val services: List<IService> = getKoin().getAll()
            services.forEach { it.update() }
        }
    }

}