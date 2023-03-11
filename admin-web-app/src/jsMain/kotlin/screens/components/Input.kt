package screens.components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.events.SyntheticKeyboardEvent
import ru.novolmob.core.models.*
import styles.Colors
import styles.Styles
import kotlin.js.RegExp


@Composable
fun passwordInput(
    password: String,
    visible: Boolean = false,
    setPassword: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    val passwordColor by remember(password) {
        derivedStateOf {
            if (password.isEmpty()) Colors.emptyInput else Colors.validInput
        }
    }

    input(
        type = if (visible) InputType.Text else InputType.Password,
        name = "password",
        label = "Пароль",
        value = password,
        borderColor = passwordColor,
        set = setPassword,
        onKeyDown = {
            if (it.key == "Enter")
                if (password.isEmpty()) it.preventDefault()
                else onSubmit()
        }
    )
}

val phoneNumberRegex = RegExp("^[+]?\\d*$")

@Composable
fun phoneNumberInput(
    phoneNumber: String,
    readOnly: Boolean = false,
    validPhoneNumber: Boolean,
    setPhoneNumber: (String) -> Unit
) {
    val phoneNumberColor by remember(phoneNumber) {
        derivedStateOf {
            if (phoneNumber.isEmpty()) Colors.emptyInput
            else if (validPhoneNumber) Colors.validInput else Colors.invalidInput
        }
    }
    var errorMessage by remember(validPhoneNumber) {
        mutableStateOf<String?>(null)
    }

    input(
        type = InputType.Text,
        name = "phone-number",
        label = "Номер телефона",
        readOnly = readOnly,
        value = phoneNumber,
        borderColor = phoneNumberColor,
        set = {
            if (phoneNumberRegex.test(it)) {
                if (it == "+") {
                    setPhoneNumber("")
                } else if (it.length == 1) {
                    setPhoneNumber("+$it")
                } else {
                    setPhoneNumber(it)
                }
            }
        },
        errorMessage = errorMessage,
        onKeyDown = {
            if (it.key == "Enter") {
                it.preventDefault()
                if (!validPhoneNumber) errorMessage = "Номер введён введён неправильно."
            }
        }
    )
}

@Composable
fun languageInput(
    language: Language?,
    languages: Map<String, Language>,
    readOnly: Boolean = false,
    setLanguage: (Language) -> Unit
) {
    select(
        initial = language,
        map = languages,
        name = "language",
        label = "Язык",
        readOnly = readOnly,
        set = setLanguage
    )
}

@Composable
fun patronymicInput(
    patronymic: String,
    readOnly: Boolean = false,
    validPatronymic: Boolean,
    setPatronymic: (String) -> Unit
) {
    val patronymicColor by remember(patronymic) {
        derivedStateOf {
            if (patronymic.isEmpty()) Colors.emptyInput
            else if (validPatronymic) Colors.validInput else Colors.invalidInput
        }
    }
    var errorMessage by remember(validPatronymic) {
        mutableStateOf<String?>(null)
    }

    input(
        type = InputType.Text,
        name = "patronymic",
        label = "Отчество",
        readOnly = readOnly,
        pattern = PatronymicChecking.regex.toString(),
        value = patronymic,
        borderColor = patronymicColor,
        errorMessage = errorMessage,
        set = setPatronymic,
        onKeyDown = {
            if (it.key == "Enter") {
                it.preventDefault()
                if (!validPatronymic) errorMessage = "Отчество введено неправильно."
            }
        }
    )
}

@Composable
fun lastnameInput(
    lastname: String,
    readOnly: Boolean = false,
    validLastname: Boolean,
    setLastname: (String) -> Unit
) {
    var writingStarted by remember {
        mutableStateOf(false)
    }
    val lastnameColor by remember(lastname) {
        derivedStateOf {
            if (!writingStarted && lastname.isNotEmpty())
                writingStarted = true

            if (!writingStarted) Colors.emptyInput
            else if (validLastname) Colors.validInput else Colors.invalidInput
        }
    }
    var errorMessage by remember(validLastname) {
        mutableStateOf<String?>(null)
    }

    input(
        type = InputType.Text,
        name = "lastname",
        label = "Фамилия",
        readOnly = readOnly,
        pattern = LastnameChecking.regex.toString(),
        value = lastname,
        borderColor = lastnameColor,
        errorMessage = errorMessage,
        set = setLastname,
        onKeyDown = {
            if (it.key == "Enter") {
                it.preventDefault()
                if (!validLastname) errorMessage = "Фамилия введена неправильно."
            }
        }
    )
}

@Composable
fun firstnameInput(
    firstname: String,
    readOnly: Boolean = false,
    validFirstname: Boolean,
    setFirstname: (String) -> Unit
) {
    var writingStarted by remember {
        mutableStateOf(false)
    }
    val firstnameColor by remember(firstname) {
        derivedStateOf {
            if (!writingStarted && firstname.isNotEmpty())
                writingStarted = true

            if (!writingStarted) Colors.emptyInput
            else if (validFirstname) Colors.validInput else Colors.invalidInput
        }
    }
    var errorMessage by remember(validFirstname) {
        mutableStateOf<String?>(null)
    }

    input(
        type = InputType.Text,
        name = "firstname",
        label = "Имя",
        readOnly = readOnly,
        pattern = FirstnameChecking.regex.toString(),
        value = firstname,
        borderColor = firstnameColor,
        errorMessage = errorMessage,
        set = setFirstname,
        onKeyDown = {
            if (it.key == "Enter") {
                it.preventDefault()
                if (!validFirstname) errorMessage = "Имя введено неправильно."
            }
        }
    )
}

@Composable
fun emailInput(
    email: String,
    readOnly: Boolean = false,
    validEmail: Boolean,
    setEmail: (String) -> Unit
) {
    val emailColor by remember(email) {
        derivedStateOf {
            if (email.isEmpty()) Colors.emptyInput
            else if (validEmail) Colors.validInput else Colors.invalidInput
        }
    }
    var errorMessage by remember(validEmail) {
        mutableStateOf<String?>(null)
    }

    input(
        type = InputType.Text,
        name = "email",
        label = "Email",
        readOnly = readOnly,
        pattern = EmailChecking.regex.toString(),
        value = email,
        borderColor = emailColor,
        errorMessage = errorMessage,
        set = setEmail,
        onKeyDown = {
            if (it.key == "Enter") {
                it.preventDefault()
                if (!validEmail) errorMessage = "Email введён неправильно."
            }
        }
    )
}

@Composable
@NonRestartableComposable
fun <K, V> select(
    initial: V?,
    map: Map<K, V>,
    name: String,
    label: String? = null,
    require: Boolean = true,
    readOnly: Boolean = false,
    set: (V) -> Unit,
) {
    label?.let {
        Label(name) { Text(it) }
    }
    Select(
        attrs = {
            attr("name", name)
            require(require)
            if (readOnly) attr("disabled", "true")
        }
    ) {
        map.forEach { (key, value) ->
            val selected by remember {
                derivedStateOf { value == initial }
            }
            Option(
                value = value.toString(),
                attrs = {
                    onClick { set(value) }
                    if (selected) selected()
                }
            ) {
               Text(key.toString())
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun <K> input(
    type: InputType<K>,
    name: String,
    label: String? = null,
    readOnly: Boolean = false,
    require: Boolean = true,
    placeholder: String? = null,
    pattern: String? = null,
    errorMessage: String? = null,
    value: String,
    borderColor: CSSColorValue,
    set: (K) -> Unit,
    onKeyDown: (SyntheticKeyboardEvent) -> Unit = {}
) {
    label?.let {
        Label(name) { Text(it) }
    }
    Input(type) {
        value(value)
        name(name)
        if (readOnly) readOnly()
        placeholder?.let(::placeholder)
        pattern?.let(::pattern)
        require(require)
        style {
            border {
                width(2.px)
                style(LineStyle.Solid)
                color(borderColor)
            }
            fontSize(15.px)
            borderRadius(30.px)
            outline("none")
            textAlign("center")
            width(250.px)
            padding(3.px, 5.px, 3.px, 5.px)
        }
        onInput {
            set(it.value)
        }
        onKeyDown(onKeyDown)
    }
    errorMessage?.let {
        Br()
        Span(
            attrs = {
                classes(Styles.errorMessageStyle)
                style {
                    marginBottom(5.px)
                }
            }
        ) {
            Text(it)
        }
    }
}