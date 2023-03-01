package modules

import org.koin.dsl.bind
import org.koin.dsl.module
import services.*

val serviceModule = module {
    single<IProfileService> {
        ProfileServiceImpl(
            workerRepository = get(),
            tokenStorage = get()
        )
    }.bind<IService>()
    single<IWorkerService> {
        WorkerServiceImpl(
            workerRepository = get()
        )
    }.bind<IService>()
    single<ICityService> {
        CityServiceImpl(
            cityRepository = get()
        )
    }.bind<IService>()
}