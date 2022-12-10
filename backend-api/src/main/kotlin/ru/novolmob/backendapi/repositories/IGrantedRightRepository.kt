package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.UserUpdateModel
import ru.novolmob.database.models.ids.UserId

interface IGrantedRightRepository: ICrudRepository<UserId, UserModel, UserCreateModel, UserUpdateModel>