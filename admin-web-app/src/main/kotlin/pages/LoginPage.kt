package pages

import csstype.*
import emotion.react.css
import react.ChildrenBuilder
import react.VFC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.useState
import ringui.*
import ru.novolmob.core.models.*
import ru.novolmob.core.models.Email.Companion.email
import ru.novolmob.core.models.Password.Companion.password
import ru.novolmob.core.models.PhoneNumber.Companion.phoneNumber
import web.html.HTMLInputElement
import web.uievents.MouseEvent

val LoginPage = VFC {
    div {
        css {
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }

        div {
            css {
                width = 30.pct
            }
            Form(
                onEmailSubmit = { email, password ->
                    console.info(email.string, password.string)
                    AlertService.addAlert(
                        message = "Email",
                        type = AlertType.message,
                        timeout = 2000
                    )
                },
                onPhoneNumberSubmit = { phonenNumber, password ->
                    console.info(phonenNumber.toString(), password.string)
                    AlertService.addAlert(
                        message = "Phone",
                        type = AlertType.message,
                        timeout = 2000
                    )
                }
            )
        }
    }
}

private fun ChildrenBuilder.Form(
    onEmailSubmit: (Email, Password) -> Unit,
    onPhoneNumberSubmit: (PhoneNumber, Password) -> Unit,
) {
    var selected by useState("email")

    Tabs {
        this.selected = selected
        onSelect = {
            selected = it
        }

        css {
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }

        Tab {
            css {
                flexGrow = number(1.0)
            }
            title = VFC {
                + "Email"
            }
            id = "email"
        }
        Tab {
            css {
                flexGrow = number(1.0)
            }
            title = VFC {
                + "Phone"
            }
            id = "phone"
        }
    }
    when (selected) {
        "email" -> EmailForm(onEmailSubmit)
        "phone" -> PhoneForm(onPhoneNumberSubmit)
    }
}

private fun ChildrenBuilder.EmailForm(
    onSubmit: (Email, Password) -> Unit
) {
    var email: String by useState("")
    var password: String by useState("")

    AuthForm(
        loginInput = {
            value = email
            placeholder = "####.####@##.##"
            label = "Электронная почта"
            onClear = {
                email = ""
            }
            if (email.isNotEmpty() && !EmailChecking.regex.test(email)) {
                error = "ERROR"
            }
            onChange = {
                email = (it.target as? HTMLInputElement)?.value ?: ""
            }
        },
        password = password,
        setPassword = { password = it },
        onSubmit = {
            onSubmit(email.email(), password.password())
        }
    )
}

private fun ChildrenBuilder.PhoneForm(
    onSubmit: (PhoneNumber, Password) -> Unit
) {
    var phoneNumber: String by useState("")
    var password: String by useState("")

    AuthForm(
        loginInput = {
            value = phoneNumber
            placeholder = "+79998765432"
            label = "Номер телефона"
            onClear = { phoneNumber = "" }
            if (phoneNumber.isNotEmpty() && !PhoneNumberChecking.regex.test(phoneNumber)) {
                error = "ERROR"
            }
            onChange = {
                val text = (it.target as? HTMLInputElement)?.value ?: ""
                if (PhoneNumberChecking.regex.test(text)) {
                    phoneNumber = if (text == "+") {
                        ""
                    } else if (text.length == 1) {
                        "+$text"
                    } else {
                        text
                    }
                }
            }
        },
        password = password,
        setPassword = { password = it },
        onSubmit = {
            phoneNumber.phoneNumber()?.let {
                onSubmit(it, password.password())
            }
        }
    )
}

private fun ChildrenBuilder.AuthForm(
    loginInput: InputProps.() -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    onSubmit: (MouseEvent) -> Unit
) {

    form {
        css {
            display = Display.flex
            justifyContent = JustifyContent.center
        }

        Input {
            multiline = false
            loginInput()
        }

        Input {
            value = password
            label = "Пароль"
            multiline = false
            onClear = { setPassword("") }
            if (password.isNotEmpty() && !PasswordChecking.regex.test(password)) {
                error = "ERROR"
            }
            onChange = {
                setPassword((it.target as? HTMLInputElement)?.value ?: "")
            }
        }

        Button {
            text = true
            onMouseDown = onSubmit
            + "Авторизоваться"
        }
    }
}