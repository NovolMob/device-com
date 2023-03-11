package screens.components

import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import styles.Colors

@Composable
fun <T> tableWithContent(
    list: List<T>,
    key: (T) -> Any,
    selected: T? = null,
    onClickTableCell: (T) -> Unit,
    convert: T.() -> Map<String, Any?>,
    content: @Composable (T) -> Unit
) {
    var onDragStart by remember {
        mutableStateOf(false)
    }
    var dragX by remember {
        mutableStateOf<Int?>(null)
    }

    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                color(Colors.generalColor)
            }
            onMouseMove {
                if (onDragStart) {
                    it.preventDefault()
                    if (it.clientX > 45) {
                        dragX = (it.clientX - 25)
                    }
                }
            }
            onMouseUp { onDragStart = false }
            onMouseLeave { onDragStart = false }
        }
    ) {
        Div(
            attrs = {
                style {
                    dragX?.let {
                        flexBasis(it.px)
                    }
                    maxWidth("max-content")
                    height(100.vh)
                    margin(10.px, 10.px, 0.px, 10.px)
                    overflow("auto")
                }
            }
        ) {
            table(
                list = list,
                isSelected = { key(it) == selected?.let(key) },
                onClick = onClickTableCell,
                convert = convert
            )
        }
        Div(attrs = {
            style {
                height(100.vh)
                flexBasis(10.px)
                backgroundColor(Color.white)
                cursor("col-resize")
            }
            onMouseDown { onDragStart = true }
        })
        Div(
            attrs = {
                style {
                    flexGrow(1)
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                    justifyContent(JustifyContent.Center)
                }
            }
        ) {
            if (selected != null) {
                content(selected)
            }
        }
    }
}

@Composable
fun <T> table(
    list: List<T>,
    isSelected: (T) -> Boolean = { false },
    onClick: (T) -> Unit = {},
    convert: T.() -> Map<String, Any?>
) {
    var tableHeader by remember {
        mutableStateOf(emptyList<String>())
    }
    var tableBody by remember {
        mutableStateOf(emptyList<Pair<T, Map<String, Any?>>>())
    }
    CoroutineScope(Dispatchers.Default).launch {
        tableBody = list.map { it to convert(it) }
        tableHeader = tableBody.maxBy { it.second.size }.second.keys.toList()
    }
    Table(
        attrs = {
            style {
                border(1.px, LineStyle.Solid, Colors.tableBorderColor)
                property("border-collapse", "collapse")
                fontSize(18.px)
            }
        }
    ) {
        Thead {
            Tr {
                tableHeader.forEach {
                    Th(
                        attrs = {
                            style {
                                padding(0.px, 5.px, 0.px, 5.px)
                                border(1.px, LineStyle.Solid, Colors.tableBorderColor)
                                property("border-collapse", "collapse")
                                whiteSpace("nowrap")
                            }
                        }
                    ) {
                        Text(it)
                    }
                }
            }
        }
        Tbody {
            tableBody.forEach { (entity, map) ->
                val selected by remember(isSelected) {
                    derivedStateOf { isSelected(entity) }
                }
                Tr(
                    attrs = {
                        style {
                            if (selected) {
                                backgroundColor(Colors.selectedRowInTableBackground)
                                color(Colors.selectedRowInTableColor)
                            } else {
                                backgroundColor(Colors.unselectedRowInTableBackground)
                                color(Colors.unselectedRowInTableColor)
                            }
                        }
                        onClick { onClick(entity) }
                    }
                ) {
                    tableHeader.forEach { value ->
                        Td(
                            attrs = {
                                style {
                                    padding(0.px, 5.px, 0.px, 5.px)
                                    border(1.px, LineStyle.Solid, Colors.tableBorderColor)
                                    property("border-collapse", "collapse")
                                }
                            }
                        ) {
                            map[value]?.let { Text(it.toString()) }
                        }
                    }
                }
            }
        }
    }
}