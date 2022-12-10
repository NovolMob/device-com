package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.WorkerCreateModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.models.WorkerUpdateModel
import ru.novolmob.database.models.ids.WorkerId

interface IWorkerRepository: ICrudRepository<WorkerId, WorkerModel, WorkerCreateModel, WorkerUpdateModel>