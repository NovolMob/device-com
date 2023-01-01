package ru.novolmob.exposedbackendapi.mappers

import arrow.core.Either
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.UserInfoModel
import ru.novolmob.exposeddatabase.tables.Users

class ResultRowUserInfoMapper: Mapper<ResultRow, UserInfoModel> {
    override fun invoke(input: ResultRow): Either<BackendException, UserInfoModel> =
        UserInfoModel(
            id = input[Users.id].value,
            firstname = input[Users.firstname],
            lastname = input[Users.lastname],
            patronymic = input[Users.patronymic],
            birthday = input[Users.birthday],
            city = input[Users.city],
            language = input[Users.language]
        ).right()
}