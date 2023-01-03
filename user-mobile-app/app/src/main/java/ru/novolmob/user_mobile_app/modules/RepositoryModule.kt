package ru.novolmob.user_mobile_app.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.user_mobile_app.repositories.IUserRepository
import ru.novolmob.user_mobile_app.repositories.UserRepositoryImpl

val repositoryModule = module {
    singleOf(::UserRepositoryImpl).bind<IUserRepository>()
}