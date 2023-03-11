package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.PointDetailCreateModel
import ru.novolmob.backendapi.models.PointDetailModel
import ru.novolmob.backendapi.models.PointDetailUpdateModel
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

interface IPointDetailRepository: IDetailRepository<PointDetailId, PointId, PointDetailModel, PointDetailCreateModel, PointDetailUpdateModel>