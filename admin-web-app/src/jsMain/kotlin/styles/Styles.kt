package styles

import org.jetbrains.compose.web.css.*

object Styles: StyleSheet() {
    val loginInputStyle by style {
        fontSize(15.px)
        borderRadius(30.px)
        outline("none")
        textAlign("center")
        width(250.px)
        padding(3.px, 5.px, 3.px, 5.px)
    }
    val inactiveLoginFormTabStyle by style {
        property("margin-left","auto")
        property("margin-right","auto")
        backgroundColor(Color.transparent)
        outline("none")
        border(0.px)
        color(Colors.generalColor)
    }
    val activeLoginFormTabStyle by style {
        property("margin-left","auto")
        property("margin-right","auto")
        backgroundColor(Color.transparent)
        outline("none")
        border(0.px)
        color(Colors.generalColor)
        property("border-bottom", "1px solid ${Colors.activeForm}")
    }
    val errorMessageStyle by style {
        color(Colors.errorMessage)
        fontSize(10.px)
    }


}