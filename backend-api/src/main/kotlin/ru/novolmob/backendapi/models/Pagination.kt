package ru.novolmob.backendapi.models

interface Pagination {
    val page: Long?
    val pageSize: Long?
    val sortByColumn: String?
    val sortOrder: String?
}