package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.UserCredentialCreateModel
import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.backendapi.models.UserCredentialUpdateModel
import ru.novolmob.database.models.ids.CredentialId

interface IUserCredentialRepository: ICrudRepository<CredentialId, UserCredentialModel, UserCredentialCreateModel, UserCredentialUpdateModel>