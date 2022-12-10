package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.PointDetailCreateModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointDetailUpdateModel
import ru.novolmob.database.models.ids.PointDetailId

interface IPointDetailRepository: ICrudRepository<PointDetailId, PointDetailModel, PointDetailCreateModel, PointDetailUpdateModel>