package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.Language

fun <ParentID: Comparable<ParentID>> detailWithParentIDAndLanguageIsExists(parentID: ParentID, language: Language) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.DETAIL_IS_EXISTS,
        message = "Detail for $parentID with language $language is exists!"
    )