package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    val page: Long,
    val size: Long,
    val list: List<T>
)