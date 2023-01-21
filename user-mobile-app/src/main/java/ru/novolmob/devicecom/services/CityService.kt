package ru.novolmob.devicecom.services

import androidx.compose.ui.text.intl.Locale
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityShortModel
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.devicecom.repositories.ICityRepository
import ru.novolmob.devicecom.utils.ScreenNotification.Companion.notice

interface ICityService: IService {
    val cities: StateFlow<List<CityShortModel>>
    fun getOrNull(cityId: CityId): CityShortModel?
}

class CityServiceImpl(
    private val cityRepository: ICityRepository,
    private val profileService: IProfileService
): ICityService {
    private val _cities = MutableStateFlow(emptyList<CityShortModel>())
    override val cities: StateFlow<List<CityShortModel>> = _cities.asStateFlow()

    init {
        serviceScope.launch {
            launch { update().notice() }
        }
    }

    override suspend fun update(): Either<AbstractBackendException, List<CityShortModel>> =
        cityRepository.getAll(
            profileService.profile.value?.language ?:
            Locale.current.language.let(::Language)
        ).flatMap {
            it.list.also { list -> _cities.update { list } }.right()
        }

    override suspend fun clear() {
        _cities.update { emptyList() }
    }

    override fun getOrNull(cityId: CityId): CityShortModel? = cities.value.find { it.id == cityId }

}