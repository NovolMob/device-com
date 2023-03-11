package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.CityDetailCreateModel
import ru.novolmob.backendapi.models.CityDetailModel
import ru.novolmob.backendapi.models.CityDetailUpdateModel
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

interface ICityDetailRepository: IDetailRepository<CityDetailId, CityId, CityDetailModel, CityDetailCreateModel, CityDetailUpdateModel>