package modules

import org.koin.dsl.module
import services.IProfileService
import services.ProfileServiceImpl

val serviceModule = module {
    single<IProfileService> {
        ProfileServiceImpl(
            workerRepository = get(),
            tokenStorage = get()
        )
    }
}