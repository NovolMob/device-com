package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.UserCredentialModel
import ru.novolmob.backendapi.models.UserCredentialUpdate
import ru.novolmob.core.models.ids.UserId

interface IUserCredentialRepository: ICredentialRepository<UserId, UserCredentialModel, UserCredentialUpdate>