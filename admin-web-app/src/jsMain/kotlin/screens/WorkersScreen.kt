package screens

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Form
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.core.models.Language
import screens.components.*
import services.IWorkerService
import styles.Colors

@Composable
fun workerScreen(
    workerService: IWorkerService,
    worker: WorkerModel
) {
    Form(
        attrs = {
            style {
                color(Colors.generalColor)
            }
        }
    ) {
        firstnameInput(
            firstname = worker.firstname.string,
            readOnly = true,
            validFirstname = true,
            setFirstname = {}
        )
        Br()
        lastnameInput(
            lastname = worker.lastname.string,
            readOnly = true,
            validLastname = true,
            setLastname = {}
        )
        Br()
        patronymicInput(
            patronymic = worker.patronymic?.string ?: "",
            readOnly = true,
            validPatronymic = true,
            setPatronymic = {}
        )
        Br()
        languageInput(
            language = worker.language,
            languages = Language.availableLanguages,
            readOnly = true,
            setLanguage = {}
        )
        Br()
        emailInput(
            email = worker.email?.string ?: "",
            readOnly = true,
            validEmail = true,
            setEmail = {}
        )
        Br()
        phoneNumberInput(
            phoneNumber = worker.phoneNumber.toString(),
            readOnly = true,
            validPhoneNumber = true,
            setPhoneNumber = {}
        )
    }
}