package ru.novolmob.jdbcdatabase.tables.parameters.values

import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType

class ProcedureParameterValue<T>(
    parameterName: String,
    value: T?,
    type: IParameterType<T>,
    val mode: ProcedureParameter.Mode?
) : ParameterValue<T>(parameterName, value, type) {
    fun isIn() = mode == ProcedureParameter.Mode.IN
    fun isOut() = mode == ProcedureParameter.Mode.OUT
}