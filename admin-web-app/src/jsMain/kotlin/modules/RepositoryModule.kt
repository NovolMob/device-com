package modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import repositories.IWorkerRepository
import repositories.WorkerRepositoryImpl

val repositoryModule = module {
    singleOf(::WorkerRepositoryImpl).bind<IWorkerRepository>()
}