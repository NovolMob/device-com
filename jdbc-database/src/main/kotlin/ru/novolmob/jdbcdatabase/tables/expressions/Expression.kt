package ru.novolmob.jdbcdatabase.tables.expressions

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.columns.IColumn
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue

sealed class Expression {
    abstract val sqlString: String
    abstract val valueOrder: List<ColumnValue<*>>

    infix fun and(second: Expression): TwoExpressions.And = TwoExpressions.And(this, second)
    infix fun or(second: Expression): TwoExpressions.Or = TwoExpressions.Or(this, second)

    class Between<T: Any>(private val column: IColumn<T>, private val start: ColumnValue<T>): Expression() {
        private lateinit var end: ColumnValue<T>

        infix fun end(end: T) = apply { this.end = column valueOf end }
        override val sqlString: String
            get() = "${column.name} ${DatabaseVocabulary.BETWEEN} ${DatabaseVocabulary.PARAMETER_SYMBOL} ${DatabaseVocabulary.AND} ${DatabaseVocabulary.PARAMETER_SYMBOL}"
        override val valueOrder: List<ColumnValue<T>>
            get() = listOf(start, end)

    }

    sealed class TwoExpressions(private val first: Expression, private val second: Expression, private val sign: String): Expression() {
        override val sqlString: String
            get() = "${first.sqlString} $sign ${second.sqlString}"
        override val valueOrder: List<ColumnValue<*>>
            get() = first.valueOrder + second.valueOrder

        class And(first: Expression, second: Expression): TwoExpressions(first, second, DatabaseVocabulary.AND)
        class Or(first: Expression, second: Expression): TwoExpressions(first, second, DatabaseVocabulary.OR)
    }

    sealed class OneParameter<T: Any>(private val column: IColumn<T>, private val sign: String, val value: ColumnValue<T>): Expression() {
        override val valueOrder: List<ColumnValue<T>>
            get() = listOf(value)
        override val sqlString: String
            get() = "${column.name} $sign ${DatabaseVocabulary.PARAMETER_SYMBOL}"

        class Equal<T: Any>(column: IColumn<T>, value: ColumnValue<T>): OneParameter<T>(column, DatabaseVocabulary.EQUAL_SYMBOL, value)
        class Less<T: Any>(column: IColumn<T>, value: ColumnValue<T>): OneParameter<T>(column, DatabaseVocabulary.LESS_SYMBOL, value)
        class LessEqual<T: Any>(column: IColumn<T>, value: ColumnValue<T>): OneParameter<T>(column, DatabaseVocabulary.LESS_EQUAL_SYMBOL, value)
        class More<T: Any>(column: IColumn<T>, value: ColumnValue<T>): OneParameter<T>(column, DatabaseVocabulary.MORE_SYMBOL, value)
        class MoreEqual<T: Any>(column: IColumn<T>, value: ColumnValue<T>): OneParameter<T>(column, DatabaseVocabulary.MORE_EQUAL_SYMBOL, value)

    }
    companion object {

        infix fun <T: Any> IColumn<T>.between(start: T): Between<T> = Between(this, this valueOf start)
        infix fun <T: Any> IColumn<T>.eq(value: T): OneParameter.Equal<T> = OneParameter.Equal(this, this valueOf value)
        infix fun <T: Any> IColumn<T>.less(value: T): OneParameter.Less<T> = OneParameter.Less(this, this valueOf value)
        infix fun <T: Any> IColumn<T>.lessEq(value: T): OneParameter.LessEqual<T> =
            OneParameter.LessEqual(this, this valueOf value)
        infix fun <T: Any> IColumn<T>.more(value: T): OneParameter.More<T> = OneParameter.More(this, this valueOf value)
        infix fun <T: Any> IColumn<T>.moreEq(value: T): OneParameter.MoreEqual<T> =
            OneParameter.MoreEqual(this, this valueOf value)

    }

}
