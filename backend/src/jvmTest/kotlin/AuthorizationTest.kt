import KtorUtil.post
import arrow.core.Either
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test
import ru.novolmob.backend.ktorrouting.user.Login
import ru.novolmob.backend.ktorrouting.user.Registration
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.LoginModel
import ru.novolmob.backendapi.models.TokenModel
import ru.novolmob.backendapi.models.UserCreateModel
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.core.models.Firstname.Companion.firstname
import ru.novolmob.core.models.Language.Companion.language
import ru.novolmob.core.models.Lastname.Companion.lastname
import ru.novolmob.core.models.Password.Companion.password
import ru.novolmob.core.models.PhoneNumber.Companion.phoneNumber

class AuthorizationTest {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(Resources)
        defaultRequest {
            url("http://localhost:8080/")
        }
    }

    @Test
    fun testRegistration() {
        runBlocking {
            val either: Either<AbstractBackendException, UserModel> = client.post(
                resource = Registration(),
                body = UserCreateModel(
                    firstname = "Кирилл".firstname(),
                    lastname = "Шиков".lastname(),
                    language = "ru".language(),
                    phoneNumber = "+7 999 876 54 32".phoneNumber()!!,
                    password = "q1".password()
                )
            )
            assert(either.isRight()) {
                either.fold(
                    ifLeft = { it.message },
                    ifRight = { it.toString() }
                )
            }
        }
    }

    @Test
    fun testRightAuthorization() {
        runBlocking {
            val either: Either<AbstractBackendException, TokenModel> = client.post(
                resource = Login(),
                body = LoginModel(
                    phoneNumber = "+7 999 876 54 32".phoneNumber()!!,
                    password = "q1".password()
                )
            )
            assert(either.isRight()) {
                either.fold(
                    ifLeft = { it.message },
                    ifRight = { it.toString() }
                )
            }
        }
    }

    @Test
    fun testBadPassAuthorization() {
        runBlocking {
            val either: Either<AbstractBackendException, TokenModel> = client.post(
                resource = Login(),
                body = LoginModel(
                    phoneNumber = "+7 999 876 54 32".phoneNumber()!!,
                    password = "q2".password()
                )
            )
            assert(either.isLeft()) {
                either.fold(
                    ifLeft = { it.message },
                    ifRight = { it.toString() }
                )
            }
        }
    }

    @Test
    fun testBadPhoneAuthorization() {
        runBlocking {
            val either: Either<AbstractBackendException, TokenModel> = client.post(
                resource = Login(),
                body = LoginModel(
                    phoneNumber = "+7 999 876 54 33".phoneNumber()!!,
                    password = "q1".password()
                )
            )
            assert(either.isLeft()) {
                either.fold(
                    ifLeft = { it.message },
                    ifRight = { it.toString() }
                )
            }
        }
    }
}