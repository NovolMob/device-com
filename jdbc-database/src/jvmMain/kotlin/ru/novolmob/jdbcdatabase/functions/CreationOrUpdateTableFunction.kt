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

    object CreationOrUpdateOrderStatusFunction: CreationOrUpdateTableFunction<OrderStatusId>(
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

    object CreationOrUpdateCityDetailFunction: CreationOrUpdateTableFunction<CityDetailId>(
        "f_id", CityDetails
    ) {
        val cityId = registerReference("f_city_id", CityDetails.parentId)
        val title = registerReference("f_title", CityDetails.title)
        val language = registerReference("f_language", CityDetails.language)

        suspend fun <T> call(
            id: CityDetailId? = null,
            cityId: CityId,
            title: Title,
            language: Language,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.cityId valueOf cityId,
                this.title valueOf title,
                this.language valueOf language,
                block = block
            )

    }

    object CreationOrUpdateDeviceDetailFunction: CreationOrUpdateTableFunction<DeviceDetailId>(
        "f_id", DeviceDetails
    ) {
        val deviceId = registerReference("f_device_id", DeviceDetails.parentId)
        val title = registerReference("f_title", DeviceDetails.title)
        val description = registerReference("f_description", DeviceDetails.description)
        val features = registerReference("f_features", DeviceDetails.features)
        val language = registerReference("f_language", DeviceDetails.language)

        suspend fun <T> call(
            id: DeviceDetailId? = null,
            deviceId: DeviceId,
            title: Title,
            description: Description,
            features: Features,
            language: Language,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.deviceId valueOf deviceId,
                this.title valueOf title,
                this.description valueOf description,
                this.features valueOf features,
                this.language valueOf language,
                block = block
            )

    }

    object CreationOrUpdateDeviceTypeDetailFunction: CreationOrUpdateTableFunction<DeviceTypeDetailId>(
        "f_id", DeviceTypeDetails
    ) {
        val deviceTypeId = registerReference("f_device_type_id", DeviceTypeDetails.parentId)
        val title = registerReference("f_title", DeviceTypeDetails.title)
        val description = registerReference("f_description", DeviceTypeDetails.description)
        val language = registerReference("f_language", DeviceTypeDetails.language)

        suspend fun <T> call(
            id: DeviceTypeDetailId? = null,
            deviceTypeId: DeviceTypeId,
            title: Title,
            description: Description,
            language: Language,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.deviceTypeId valueOf deviceTypeId,
                this.title valueOf title,
                this.description valueOf description,
                this.language valueOf language,
                block = block
            )

    }

    object CreationOrUpdateOrderStatusDetailFunction: CreationOrUpdateTableFunction<OrderStatusDetailId>(
        "f_id", OrderStatusDetails
    ) {
        val orderStatusId = registerReference("f_order_status_id", OrderStatusDetails.parentId)
        val title = registerReference("f_title", OrderStatusDetails.title)
        val description = registerReference("f_description", OrderStatusDetails.description)
        val language = registerReference("f_language", OrderStatusDetails.language)

        suspend fun <T> call(
            id: OrderStatusDetailId? = null,
            orderStatusId: OrderStatusId,
            title: Title,
            description: Description,
            language: Language,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.orderStatusId valueOf orderStatusId,
                this.title valueOf title,
                this.description valueOf description,
                this.language valueOf language,
                block = block
            )

    }

    object CreationOrUpdatePointDetailFunction: CreationOrUpdateTableFunction<PointDetailId>(
        "f_id", PointDetails
    ) {
        val pointId = registerReference("f_point_id", PointDetails.parentId)
        val address = registerReference("f_address", PointDetails.address)
        val schedule = registerReference("f_schedule", PointDetails.schedule)
        val description = registerReference("f_description", PointDetails.description)
        val language = registerReference("f_language", PointDetails.language)

        suspend fun <T> call(
            id: PointDetailId? = null,
            pointId: PointId,
            address: Address,
            schedule: Schedule,
            description: Description,
            language: Language,
            block: suspend ResultSet.() -> T
        ): T =
            callWithResultSet(
                this.id valueOf id,
                this.pointId valueOf pointId,
                this.address valueOf address,
                this.schedule valueOf schedule,
                this.description valueOf description,
                this.language valueOf language,
                block = block
            )

    }

}