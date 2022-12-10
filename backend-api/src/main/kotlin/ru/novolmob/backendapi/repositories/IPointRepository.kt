package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.PointCreateModel
import ru.novolmob.backendapi.models.PointModel
import ru.novolmob.backendapi.models.PointUpdateModel
import ru.novolmob.database.models.ids.PointId

interface IPointRepository: ICrudRepository<PointId, PointModel, PointCreateModel, PointUpdateModel>