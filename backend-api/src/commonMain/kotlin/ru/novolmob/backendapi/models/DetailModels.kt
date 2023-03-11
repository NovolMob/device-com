package ru.novolmob.backendapi.models

import ru.novolmob.core.models.Language

interface DetailCreate<ParentID: Comparable<ParentID>> {
    val parentId: ParentID
    val language: Language
}

interface DetailUpdate<ParentID: Comparable<ParentID>> {
    val parentId: ParentID?
    val language: Language?
}