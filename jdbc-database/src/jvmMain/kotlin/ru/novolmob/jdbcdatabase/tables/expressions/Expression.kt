package ru.novolmob.jdbcdatabase.tables.expressions

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.fullName
import ru.novolmob.jdbcdatabase.tables.parameters.IParameter
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

sealed class Expression {
    abstract val sqlString: String
    abstract val valueOrder: List<ParameterValue<*>>

    infix fun and(second: Expression): TwoExpressions.And = TwoExpressions.And(this, second)
    infix fun or(second: Expression): TwoExpressions.Or = TwoExpressions.Or(this, second)
    class In<T: Any>(private val column: IParameter<T>, private val list: List<ParameterValue<T>>): Expression() {
        override val sqlString: String
            get() = DatabaseVocabulary.inList(column.fullName(), list.size)
        override val valueOrder: List<ParameterValue<*>>
            get() = list

    }

    class Between<T: Any>(private val column: IParameter<T>, private val start: ParameterValue<T>): Expression() {
        private lateinit var end: ParameterValue<T>

        infix fun end(end: T) = apply { this.end = column valueOf end }
        override val sqlString: String
            get() = DatabaseVocabulary.between(column.fullName())
        override val valueOrder: List<ParameterValue<T>>
            get() = listOf(start, end)

    }

    sealed class TwoExpressions(private val first: Expression, private val second: Expression, private val sign: String): Expression() {
        override val sqlString: String
            get() = "${first.sqlString} $sign ${second.sqlString}"
        override val valueOrder: List<ParameterValue<*>>
            get() = first.valueOrder + second.valueOrder

        class And(first: Expression, second: Expression): TwoExpressions(first, second, "AND")
        class Or(first: Expression, second: Expression): TwoExpressions(first, second, "OR")
    }

    sealed class OneParameter<T: Any>(private val column: IParameter<T>, private val sign: String, val value: ParameterValue<T>): Expression() {
        override val valueOrder: List<ParameterValue<T>>
            get() = listOf(value)
        override val sqlString: String
            get() = "${column.name} $sign ${DatabaseVocabulary.PARAMETER_SYMBOL}"

        class Equal<T: Any>(column: IParameter<T>, value: ParameterValue<T>): OneParameter<T>(column, DatabaseVocabulary.EQUAL_SYMBOL, value)
        class Less<T: Any>(column: IParameter<T>, value: ParameterValue<T>): OneParameter<T>(column, DatabaseVocabulary.LESS_SYMBOL, value)
        class LessEqual<T: Any>(column: IParameter<T>, value: ParameterValue<T>): OneParameter<T>(column, DatabaseVocabulary.LESS_EQUAL_SYMBOL, value)
        class More<T: Any>(column: IParameter<T>, value: ParameterValue<T>): OneParameter<T>(column, DatabaseVocabulary.MORE_SYMBOL, value)
        class MoreEqual<T: Any>(column: IParameter<T>, value: ParameterValue<T>): OneParameter<T>(column, DatabaseVocabulary.MORE_EQUAL_SYMBOL, value)

    }
    companion object {

        infix fun <T: Any> IParameter<T>.between(start: T): Between<T> = Between(this, this valueOf start)
        infix fun <T: Any> IParameter<T>.eq(value: T): OneParameter.Equal<T> = OneParameter.Equal(this, this valueOf value)
        infix fun <T: Any> IParameter<T>.less(value: T): OneParameter.Less<T> = OneParameter.Less(this, this valueOf value)
        infix fun <T: Any> IParameter<T>.lessEq(value: T): OneParameter.LessEqual<T> =
            OneParameter.LessEqual(this, this valueOf value)
        infix fun <T: Any> IParameter<T>.more(value: T): OneParameter.More<T> = OneParameter.More(this, this valueOf value)
        infix fun <T: Any> IParameter<T>.moreEq(value: T): OneParameter.MoreEqual<T> =
            OneParameter.MoreEqual(this, this valueOf value)
        infix fun <T: Any> IParameter<T>.inList(list: List<T>): In<T> = In(this, list.map { valueOf(it) })

    }

}
