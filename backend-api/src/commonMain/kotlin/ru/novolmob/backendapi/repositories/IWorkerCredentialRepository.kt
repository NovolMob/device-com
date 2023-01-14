package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.WorkerCredentialModel
import ru.novolmob.backendapi.models.WorkerCredentialUpdate
import ru.novolmob.core.models.ids.WorkerId

interface IWorkerCredentialRepository: ICredentialRepository<WorkerId, WorkerCredentialModel, WorkerCredentialUpdate>