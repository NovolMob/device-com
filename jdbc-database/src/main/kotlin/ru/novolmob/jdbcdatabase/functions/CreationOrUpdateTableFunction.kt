package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.*
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.*
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.FunctionParameter
import java.sql.ResultSet

sealed class CreationOrUpdateTableFunction<ID: Comparable<ID>>(
    idName: String,
    val table: IdTable<ID>
): RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {
    private val _references = mutableListOf<Pair<FunctionParameter<*>, Column<*>>>()
    val references
        get() = _references.toList()
    val id: FunctionParameter<ID> = registerParameter(idName, table.id.type)

    fun <T: Any> registerReference(name: String, column: Column<T>): FunctionParameter<T> =
        registerParameter(name, column.type).also { _references.add(it to column) }

    override fun body(): String =
        "DECLARE\n" +
                "    new_id ${id.type.databaseType};\n" +
                "    ref refcursor DEFAULT 'cursor_$name';\n" +
                "BEGIN\n" +
                "    IF ($id IS NOT NULL) THEN\n" +
                "        UPDATE $table SET " +
                references.joinToString {
                    it.second.name + " = " + it.first
                } +
                " WHERE ${table.id} = $id;\n" +
                "        OPEN ref FOR SELECT * FROM $table WHERE ${table.id} = $id;\n" +
                "    ELSE\n" +
                "        INSERT INTO $table (" +
                references.joinToString { it.second.name } +
                ") VALUES (" + references.joinToString { it.first.toString() } + ") RETURNING ${table.id} INTO new_id;\n" +
                "        OPEN ref FOR SELECT * FROM $table WHERE ${table.id} = new_id;\n" +
                "    END IF;\n" +
                "    RETURN ref;\n" +
                "END;"

    object CreationOrUpdateGranterRightFunction: CreationOrUpdateTableFunction<GrantedRightId>(
        "f_id", GrantedRights
    ) {
        val workerId = registerReference("f_worker_id", GrantedRights.workerId)
        val code = registerReference("f_code", GrantedRights.code)
        val adminId = registerReference("f_admin_id", GrantedRights.adminId)

        suspend fun <T> call(
            id: GrantedRightId? = null,
            workerId: WorkerId,
            code: Code,
            adminId: WorkerId,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.workerId valueOf workerId,
                this.code valueOf code,
                this.adminId valueOf adminId,
                block = block
            )
    }

    object CreationOrUpdateCityFunction: CreationOrUpdateTableFunction<CityId>(
        "f_id", Cities
    ) {
        suspend fun <T> call(
            id: CityId? = null,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                block = block
            )
    }

    object CreationOrUpdateDeviceFunction: CreationOrUpdateTableFunction<DeviceId>(
        "f_id", Devices
    ) {
        val code = registerReference("f_code", Devices.code)
        val typeId = registerReference("f_type_id", Devices.typeId)
        val amount = registerReference("f_amount", Devices.amount)
        val price = registerReference("f_price", Devices.price)

        suspend fun <T> call(
            id: DeviceId? = null,
            code: Code,
            typeId: DeviceTypeId? = null,
            amount: Amount,
            price: Price,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.code valueOf code,
                this.typeId valueOf typeId,
                this.amount valueOf amount,
                this.price valueOf price,
                block = block
            )
    }

    object CreationOrUpdateDeviceTypeFunction: CreationOrUpdateTableFunction<DeviceTypeId>(
        "f_id", DeviceTypes
    ) {

        suspend fun <T> call(
            id: DeviceTypeId? = null,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                block = block
            )
    }

    object CreationOrUpdatePointFunction: CreationOrUpdateTableFunction<PointId>(
        "f_id", Points
    ) {
        val cityId = registerReference("f_city_id", Points.cityId)

        suspend fun <T> call(
            id: PointId? = null,
            cityId: CityId,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.cityId valueOf cityId,
                block = block
            )
    }

    object CreateOrUpdateOrderStatusFunction: CreationOrUpdateTableFunction<OrderStatusId>(
        "f_id", OrderStatuses
    ) {
        val active = registerReference(name = "f_active", OrderStatuses.active)

        suspend fun <T> call(
            id: OrderStatusId? = null,
            active: Boolean,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.active valueOf active,
                block = block
            )

    }

}