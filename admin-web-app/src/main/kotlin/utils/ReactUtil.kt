package utils

import react.VFC
import react.create
import react.router.Navigate
import react.router.NavigateProps

fun navigateElement(fc: NavigateProps.() -> Unit) = VFC { Navigate { apply(fc) } }.create()