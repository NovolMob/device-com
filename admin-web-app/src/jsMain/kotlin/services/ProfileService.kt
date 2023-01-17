package services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import naavigations.Navigation
import repositories.IWorkerRepository
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.*
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import storages.TokenStorage
import utils.EitherUtil.loginChecking


interface IProfileService: IService {
    val profile: StateFlow<WorkerModel?>

    fun loginByEmail(email: Email, password: Password)
    fun loginByPhoneNumber(phoneNumber: PhoneNumber, password: Password)
    fun logout()
    fun get(): WorkerModel?
}

class ProfileServiceImpl(
    val workerRepository: IWorkerRepository,
    val tokenStorage: TokenStorage
): IProfileService, AbstractService() {
    private val _profile = MutableStateFlow<WorkerModel?>(null)
    override val profile: StateFlow<WorkerModel?> = _profile.asStateFlow()

    init {
        serviceScope.launch {
            startLoading()
            update().loginChecking {
                loginAction()
            }
            stopLoading()
        }
    }

    private fun loginAction(tokenModel: TokenModel? = null) {
        tokenModel?.let(tokenStorage::set)
        Navigation.toMain()
    }
    private fun logoutAction() {
        tokenStorage.set(null)
        Navigation.toLogin()
    }
    private fun profile(profileModel: WorkerModel?) = _profile.update { profileModel }

    override suspend fun update(): Either<AbstractBackendException, WorkerModel> =
        workerRepository.get().flatMap { userModel ->
            profile(userModel)
            userModel.right()
        }

    override suspend fun clear() {
        logoutAction()
    }

    override fun loginByEmail(
        email: Email,
        password: Password
    ) {
        serviceScope.launch {
            workerRepository.login(email = email, password = password)
                .flatMap {
                    loginAction(it)
                    update()
                }
        }
    }

    override fun loginByPhoneNumber(
        phoneNumber: PhoneNumber,
        password: Password
    ) {
        serviceScope.launch {
            workerRepository.login(phoneNumber = phoneNumber, password = password)
                .flatMap {
                    loginAction(it)
                    update()
                }
        }
    }

    override fun logout() {
        serviceScope.launch {
            workerRepository.logout().flatMap {
                logoutAction()
                Unit.right()
            }
        }
    }

    override fun get(): WorkerModel? = _profile.value

}



