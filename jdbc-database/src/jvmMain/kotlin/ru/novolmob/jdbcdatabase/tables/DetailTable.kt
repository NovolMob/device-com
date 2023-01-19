package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Language
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.Constraint
import java.sql.ResultSet

abstract class DetailTable<ID: Comparable<ID>, ParentID: Comparable<ParentID>>(name: String? = null): IdTable<ID>(name) {
    abstract val parentId: Constraint<ParentID>
    abstract val language: Column<Language>

    suspend fun <T> selectByParentIdAndLanguage(
        parentID: ParentID,
        language: Language,
        block: suspend ResultSet.() -> T
    ) = select(
        expression = (this.parentId eq parentID) and (this.language eq language),
        block = block
    )

    suspend fun <T> selectByParentId(
        parentID: ParentID,
        block: suspend ResultSet.() -> T
    ) = select(
        expression = (this.parentId eq parentID),
        block = block
    )

    suspend fun deleteByParentId(
        parentID: ParentID
    ) = delete(expression = (this.parentId eq parentID))

    suspend fun check(
        id: ID? = null,
        parentID: ParentID,
        language: Language
    ): Boolean {
        return selectByParentIdAndLanguage(parentID, language) {
            if (next())
                get(this@DetailTable.id) == id && !next()
            else true
        }
    }

}