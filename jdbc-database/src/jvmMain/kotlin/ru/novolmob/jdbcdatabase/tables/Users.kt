package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.birthdate
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.firstname
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.lastname
import ru.novolmob.jdbcdatabase.extensions.TableExtension.patronymic
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object Users: IdTable<UserId>() {

    override val id = idColumn(constructor = ::UserId).primaryKey()
    val firstname = firstname()
    val lastname = lastname()
    val patronymic = patronymic().nullable()
    val birthday = birthdate().nullable()
    val cityId = reference("city_id", Cities.id).nullable()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: UserId? = null,
        firstname: Firstname,
        lastname: Lastname,
        patronymic: Patronymic? = null,
        birthday: Birthday? = null,
        cityId: CityId? = null,
        language: Language
    ) {
        val list = mutableListOf(
            this.firstname valueOf firstname,
            this.lastname valueOf lastname,
            this.language valueOf language
        )
        id?.let { list.add(this.id valueOf it) }
        patronymic?.let { list.add(this.patronymic valueOf it) }
        birthday?.let { list.add(this.birthday valueOf it) }
        cityId?.let { list.add(this.cityId valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: UserId,
        firstname: Firstname,
        lastname: Lastname,
        patronymic: Patronymic? = null,
        birthday: Birthday? = null,
        cityId: CityId? = null,
        language: Language
    ) {
        update(
            newValues = arrayOf(
                this.firstname valueOf firstname,
                this.lastname valueOf lastname,
                this.patronymic valueOf patronymic,
                this.birthday valueOf birthday,
                this.cityId valueOf cityId,
                this.language valueOf language
            ), expression = this.id eq id
        )
    }

    suspend fun update(
        id: UserId,
        firstname: Firstname? = null,
        lastname: Lastname? = null,
        patronymic: Patronymic? = null,
        birthday: Birthday? = null,
        cityId: CityId? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        firstname?.let { list.add(this.firstname valueOf it) }
        lastname?.let { list.add(this.lastname valueOf it) }
        patronymic?.let { list.add(this.patronymic valueOf it) }
        birthday?.let { list.add(this.birthday valueOf it) }
        cityId?.let { list.add(this.cityId valueOf it) }
        language?.let { list.add(this.language valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}