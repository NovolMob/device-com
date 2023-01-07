package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.PointShortModel
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.user_mobile_app.repositories.IPointRepository
import ru.novolmob.user_mobile_app.utils.ScreenNotification.Companion.notice

interface IPointService: IService {
    val points: StateFlow<List<PointShortModel>>

    fun getOrNull(pointId: PointId): PointShortModel?
    suspend fun getAllByUserCity(): Either<AbstractBackendException, List<PointShortModel>>
}

class PointServiceImpl(
    val pointRepository: IPointRepository
): IPointService {
    private val _points = MutableStateFlow(emptyList<PointShortModel>())
    override val points: StateFlow<List<PointShortModel>> = _points.asStateFlow()

    init {
        serviceScope.launch {
            launch {
                update().notice()
            }
        }
    }

    override fun getOrNull(pointId: PointId): PointShortModel? =
        points.value.find { it.id == pointId }

    override suspend fun update(): Either<AbstractBackendException, Any> =
        pointRepository.getAll().flatMap {
            it.list.also { list -> _points.update { list } }.right()
        }

    override suspend fun getAllByUserCity(): Either<AbstractBackendException, List<PointShortModel>> =
        pointRepository.getAllByUserCity()

}