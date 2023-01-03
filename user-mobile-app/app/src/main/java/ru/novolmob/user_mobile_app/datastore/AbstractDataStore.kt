package ru.novolmob.user_mobile_app.datastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class AbstractDataStore {
    val dataStoreCoroutineScope = CoroutineScope(SupervisorJob())
}