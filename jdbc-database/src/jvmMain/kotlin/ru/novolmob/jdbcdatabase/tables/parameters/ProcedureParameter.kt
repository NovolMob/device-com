package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ProcedureParameterValue

open class ProcedureParameter<T: Any>(
    override val name: String,
    override val type: IParameterType<T>,
    override val databaseObject: DatabaseObject
): AbstractParameter<T>() {
    private var default: String = ""
    private var mode: Mode? = null

    fun default(sql: String) = apply { default = sql }
    fun inMode() = apply { mode = Mode.IN }
    fun isIn() = mode == Mode.IN
    fun isOut() = mode == Mode.OUT
    fun outMode() = apply { mode = Mode.OUT }
    override fun buildCreationString(): String {
        val list = mutableListOf<String>()

        if (mode != null) list.add(mode!!.sql)

        list.add(name)
        list.add(type.databaseType)

        if (default.isNotEmpty()) list.add(DatabaseVocabulary.default(default))

        return list.joinToString(" ")
    }

    override infix fun valueOf(value: T?): ProcedureParameterValue<T> = ProcedureParameterValue(name, value, type, mode)

    enum class Mode(val sql: String) {
        IN("in"),
        OUT("out");
    }

}