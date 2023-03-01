package screens

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.events.SyntheticKeyboardEvent
import ru.novolmob.core.models.Email.Companion.email
import ru.novolmob.core.models.EmailChecking
import ru.novolmob.core.models.Password.Companion.password
import ru.novolmob.core.models.PhoneNumber.Companion.phoneNumber
import ru.novolmob.core.models.PhoneNumberChecking
import services.IProfileService
import styles.Colors
import styles.Styles
import styles.Styles.activeLoginFormTabStyle
import styles.Styles.inactiveLoginFormTabStyle
import kotlin.js.RegExp

@Composable
fun loginScreen(
    profileService: IProfileService
) {
    val loading by profileService.loading.collectAsState()

    Div(
        attrs = {
            style {
                position(Position.Fixed)
                top(0.px)
                left(0.px)
                width(100.percent)
                height(100.percent)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
                backgroundColor(Colors.generalBackground)
                color(Colors.generalColor)
            }
        }
    ) {
        header()
        if (loading) {
            Div(
                attrs = {
                    style {
                        margin(10.px)
                        color(Color.white)
                    }
                    classes("dot-spin")
                }
            )
        } else {
            divider()
            form(
                byEmail = { email, password ->
                    profileService.loginByEmail(email.email(), password.password())
                },
                byPhoneNumber = { phoneNumber, password ->
                    phoneNumber.phoneNumber()?.let {
                        profileService.loginByPhoneNumber(it, password.password())
                    }
                }
            )
        }
    }
}

@Composable
private fun header() {
    Div(
        attrs = {
            style {
                margin(10.px)
                textAlign("right")
            }
        }
    ) {
        H1(
            attrs = {
                style {
                    margin(0.px)
                    fontSize(70.px)
                }
            }
        ) { Text("Авторизация") }
        H4(
            attrs = {
                style {
                    margin(0.px)
                }
            }
        ) { Text("by NovolMob") }
    }
}

@Composable
private fun divider() {
    Div(
        attrs = {
            style {
                height(200.px)
                border {
                    color(Color.white)
                    style(LineStyle.Solid)
                    width(3.px)
                }
            }
        }
    )
}

@Composable
private fun form(
    byEmail: (String, String) -> Unit,
    byPhoneNumber: (String, String) -> Unit,
) {
    var emailFormEnable by remember {
        mutableStateOf(true)
    }
    Div(
        attrs = {
            style {
                margin(10.px)
            }
        }
    ) {
        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                    justifyContent(JustifyContent.Center)
                }
            }
        ) {
            Button(
                attrs = {
                    classes(
                        if (emailFormEnable) activeLoginFormTabStyle
                        else inactiveLoginFormTabStyle
                    )
                    onClick { emailFormEnable = true }
                }
            ) {
                Text("по Email")
            }
            Button(
                attrs = {
                    classes(
                        if (emailFormEnable) inactiveLoginFormTabStyle
                        else activeLoginFormTabStyle
                    )
                    onClick { emailFormEnable = false }
                }
            ) {
                Text("по Номеру телефона")
            }
        }
        if (emailFormEnable) {
            emailForm(byEmail)
        } else {
            phoneNumberForm(byPhoneNumber)
        }
    }
}

@Composable
private fun phoneNumberForm(
    onSubmit: (String, String) -> Unit
) {
    var phoneNumber by remember {
        mutableStateOf("")
    }
    val validPhoneNumber by remember(phoneNumber) {
        derivedStateOf { PhoneNumberChecking.matches(phoneNumber) }
    }

    var password by remember {
        mutableStateOf("")
    }

    val activeButton by remember(phoneNumber, password) {
        derivedStateOf {
            validPhoneNumber && password.isNotEmpty()
        }
    }

    Form( attrs =  {
        onSubmit {
            if (activeButton) {
                onSubmit(phoneNumber, password)
            } else it.preventDefault()
        }
        style {
            margin(10.px)
            fontSize(20.px)
            textAlign("center")
        }
        method(FormMethod.Dialog)
    } ) {
        phoneNumberInput(
            phoneNumber = phoneNumber,
            validPhoneNumber = validPhoneNumber,
            setPhoneNumber = { phoneNumber = it }
        )
        Br()
        passwordInput(
            password = password,
            setPassword = { password = it },
            onSubmit = {
                if (activeButton) {
                    onSubmit(phoneNumber, password)
                }
            }
        )
        Br()
        submitInput(
            activeButton = activeButton,
        )
    }
}

@Composable
private fun emailForm(
    onSubmit: (String, String) -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }
    val validEmail by remember(email) {
        derivedStateOf { EmailChecking.matches(email) }
    }

    var password by remember {
        mutableStateOf("")
    }

    val activeButton by remember(email, password) {
        derivedStateOf {
            validEmail && password.isNotEmpty()
        }
    }

    Form( attrs =  {
        onSubmit {
            if (activeButton) {
                onSubmit(email, password)
            } else it.preventDefault()
        }
        style {
            margin(10.px)
            fontSize(20.px)
            textAlign("center")
        }
        method(FormMethod.Dialog)
    } ) {
        emailInput(
            email = email,
            validEmail = validEmail,
            setEmail = { email = it }
        )
        Br()
        passwordInput(
            password = password,
            setPassword = { password = it },
            onSubmit = {
                if (activeButton) {
                    onSubmit(email, password)
                }
            }
        )
        Br()
        submitInput(
            activeButton = activeButton,
        )
    }
}

@Composable
private fun submitInput(
    activeButton: Boolean
) {
    val buttonColor by remember(activeButton) {
        derivedStateOf {
            if (activeButton) Colors.activeButton else Colors.inactiveButton
        }
    }

    Div(
        attrs = {
            style {
                width(100.percent)
                height(100.percent)
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.Center)
            }
        }
    ) {
        SubmitInput {
            style {
                margin(10.px)
                padding(5.px, 15.px, 5.px, 15.px)
                backgroundColor(buttonColor)
                fontSize(15.px)
                borderRadius(30.px)
            }
        }
    }
}

@Composable
private fun passwordInput(
    password: String,
    setPassword: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    val passwordColor by remember(password) {
        derivedStateOf {
            if (password.isEmpty()) Colors.emptyInput else Colors.validInput
        }
    }

    input(
        type = InputType.Password,
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
private fun phoneNumberInput(
    phoneNumber: String,
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
private fun emailInput(
    email: String,
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
private fun <K> input(
    type: InputType<K>,
    name: String,
    label: String? = null,
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
    Br()
    Input(type) {
        value(value)
        name(name)
        placeholder?.let(::placeholder)
        pattern?.let(::pattern)
        require(true)
        style {
            border {
                width(2.px)
                style(LineStyle.Solid)
                color(borderColor)
            }
        }
        classes(Styles.loginInputStyle)
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