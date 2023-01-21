package ru.novolmob.devicecom.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.core.models.ids.CityId

class CityMutableValue(
    cities: List<CityShortModel> = emptyList(),
    cityId: CityId? = null,
    private val notValidException: AbstractBackendException = AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Город указан неверно!"
    )
): AbstractMutableValue<CityShortModel?>(
    initial = cities.find { it.id == cityId },
    initialValid = true
) {
    val cities = MutableStateFlow(cities)
    private val cityId = MutableStateFlow(cityId)


    init {
        CoroutineScope(SupervisorJob()).launch {
            launch {
                this@CityMutableValue.cities.collectLatest {
                    set(it.find { it.id == this@CityMutableValue.cityId.value })
                }
            }
        }
    }

    override fun isValid(value: CityShortModel?): Boolean = value == null || cities.value.find { it.id == value.id } != null

    override fun clear() = _value.update { null }

    fun set(cityId: CityId?) {
        this.cityId.update { cityId }
        val value = cities.value.find { it.id == cityId }
        this._value.update { value }
        this._valid.update { value == null }
    }

    override fun set(newValue: CityShortModel?) {
        this.cityId.update { newValue?.id }
        super.set(newValue)
    }

    fun getModel(): Either<AbstractBackendException, CityShortModel?> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else value.right()
        }

}