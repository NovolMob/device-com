package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Code
import ru.novolmob.core.models.ids.GrantedRightId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.code
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.inList
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import java.sql.ResultSet

object GrantedRights: IdTable<GrantedRightId>() {

    override val id = idColumn(constructor = ::GrantedRightId)
    val workerId = reference("worker_id", Workers.id).primaryKey()
    val code = code().primaryKey()
    val adminId = reference("admin_id", Workers.id)
    val creationTime = creationTime()

    suspend fun insert(
        id: GrantedRightId? = null,
        workerId: WorkerId,
        code: Code,
        adminId: WorkerId
    ) {
        val list = mutableListOf(
            this.workerId valueOf workerId,
            this.code valueOf code,
            this.adminId valueOf adminId
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: GrantedRightId,
        workerId: WorkerId? = null,
        code: Code? = null,
        adminId: WorkerId? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        workerId?.let { list.add(this.workerId valueOf it) }
        code?.let { list.add(this.code valueOf it) }
        adminId?.let { list.add(this.adminId valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

    suspend fun contains(
        workerId: WorkerId,
        codes: List<Code>
    ): Boolean = isExists((this.workerId eq workerId) and (this.code inList codes))

    suspend fun remove(
        workerId: WorkerId,
        code: Code
    ): Int = delete(expression = (this.workerId eq workerId) and (this.code eq code))

    suspend fun <T> getAllRightsFor(
        workerId: WorkerId,
        block: suspend ResultSet.() -> T
    ): T = select(expression = this.workerId eq workerId, block = block)

}