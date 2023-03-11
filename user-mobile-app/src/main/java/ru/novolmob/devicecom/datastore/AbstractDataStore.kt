package ru.novolmob.devicecom.datastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class AbstractDataStore {
    val dataStoreCoroutineScope = CoroutineScope(SupervisorJob())
}