package services

import arrow.core.Either
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repositories.ICityRepository
import ru.novolmob.backend.ktorrouting.worker.Cities
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.CityCreateModel
import ru.novolmob.backendapi.models.CityModel
import ru.novolmob.backendapi.models.CityUpdateModel
import ru.novolmob.core.models.ids.CityId
import utils.EitherUtil.ifRight

interface ICityService: IService {
    val cities: StateFlow<List<CityModel>>

    fun createCity(city: CityCreateModel)
    fun updateCity(id: CityId, city: CityUpdateModel)
    fun deleteCity(id: CityId)
}

class CityServiceImpl(
    val cityRepository: ICityRepository
): ICityService, AbstractService() {
    private val _cities = MutableStateFlow(emptyList<CityModel>())
    override val cities: StateFlow<List<CityModel>> = _cities.asStateFlow()

    override fun createCity(city: CityCreateModel) {
        serviceScope.launch {
            cityRepository.post(city).ifRight { new ->
                _cities.update { (it + new).distinctBy { it.id } }
            }
        }
    }

    override fun updateCity(id: CityId, city: CityUpdateModel) {
        serviceScope.launch {
            cityRepository.put(id, city).ifRight { new ->
                _cities.update { it.map { if (new.id == id) new else it } }
            }
        }
    }

    override fun deleteCity(id: CityId) {
        serviceScope.launch {
            cityRepository.delete(id).ifRight {
                if (it) {
                    _cities.update { it.mapNotNull { if (it.id == id) null else it } }
                }
            }
        }
    }

    override suspend fun update(): Either<AbstractBackendException, Any> =
        cityRepository.getAll(Cities()).ifRight { cities ->
            _cities.update { cities.list }
            cities
        }

    override suspend fun clear() {
        _cities.update { emptyList() }
    }

}

