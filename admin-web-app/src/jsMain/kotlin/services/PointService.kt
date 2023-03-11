package services

import kotlinx.coroutines.flow.StateFlow
import ru.novolmob.backendapi.models.PointCreateModel
import ru.novolmob.backendapi.models.PointModel
import ru.novolmob.backendapi.models.PointUpdateModel
import ru.novolmob.core.models.ids.PointId

interface IPointService: IService {
    val points: StateFlow<List<PointModel>>

    fun createPoint(point: PointCreateModel)
    fun updatePoint(id: PointId, point: PointUpdateModel)
    fun deletePoint(id: PointId)
}

class PointServiceImpl(

)

