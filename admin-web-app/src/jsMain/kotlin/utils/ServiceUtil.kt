package utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import services.IService

object ServiceUtil: KoinComponent {

    fun updateAll() {
        CoroutineScope(Dispatchers.Default).launch {
            val services: List<IService> = getKoin().getAll()
            services.forEach { it.update() }
        }
    }

    fun clearAll() {
        CoroutineScope(Dispatchers.Default).launch {
            val services: List<IService> = getKoin().getAll()
            services.forEach { it.clear() }
        }
    }

}