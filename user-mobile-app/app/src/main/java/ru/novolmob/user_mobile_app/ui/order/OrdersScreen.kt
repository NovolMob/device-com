@file:OptIn(ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.ui.order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import org.koin.androidx.compose.getViewModel
import ru.novolmob.backendapi.models.OrderItemShortModel
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.core.extensions.LocalDateTimeExtension.now
import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.user_mobile_app.R
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val state by viewModel.state.collectAsState()
    var selectedOrderForCancellation by remember {
        mutableStateOf<OrderShortModel?>(null)
    }
    val hiddenAlertDialog by remember(selectedOrderForCancellation) {
        derivedStateOf { selectedOrderForCancellation == null }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(vertical = 5.dp, horizontal = 15.dp),
    ) {
        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.LightGray
            )
        } else {
            LazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(bottom = 65.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(
                    items = state.orders,
                    key = { it.id.uuid }
                ) {
                    Order(
                        modifier = Modifier
                            .fillMaxWidth(),
                        order = it,
                        cancelOrder = { selectedOrderForCancellation = it }
                    )
                }
            }
            if (!hiddenAlertDialog) {
                CancelAlertDialog(
                    modifier = Modifier,
                    order = selectedOrderForCancellation!!,
                    onDismiss = {
                        selectedOrderForCancellation = null
                    },
                    onConfirm = {
                        selectedOrderForCancellation?.id?.let(viewModel::cancelOrder)
                        selectedOrderForCancellation = null
                    }
                )
            }
        }
    }
}

@Composable
private fun CancelAlertDialog(
    modifier: Modifier = Modifier,
    order: OrderShortModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp)),
        onDismissRequest = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.order_cancellation_confirmation)
                    .format(order.creationTime.dateTime.date
                        .toJavaLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                    ),
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(MutableInteractionSource(), indication = null, onClick = onDismiss),
                    text = stringResource(id = R.string.cancel),
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(MutableInteractionSource(), indication = null, onClick = onConfirm),
                    text = stringResource(id = R.string.confirm),
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}

@Preview
@Composable
private fun Order(
    modifier: Modifier = Modifier.fillMaxWidth(),
    order: OrderShortModel = OrderShortModel(
        id = OrderId(UUID.fromString("25e61d0c-2af5-4a5c-98ff-920f4730d4f8")),
        point = Address("г.Великий Новгород ул.Ломоносова д.11"),
        list = listOf(
            OrderItemShortModel(
                deviceId = DeviceId(UUID.fromString("4e77c4a9-a0df-420a-b1ed-00c608f01e42")),
                title = Title("б Второе устройство 2"),
                amount = Amount(1),
                priceForOne = Price(1999.0.toBigDecimal())
            )
        ),
        totalCost = Price(1999.0.toBigDecimal()),
        status = null,
        active = true,
        creationTime = CreationTime(LocalDateTime.now())
    ),
    cancelOrder: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(15.dp))
            .background(color = Color.White, shape = RoundedCornerShape(15.dp))
    ) {
        OrderHeader(
            modifier = Modifier
                .fillMaxWidth(),
            status = order.status,
            active = order.active,
            orderId = order.id,
            creationTime = order.creationTime
        )
        Devices(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            list = order.list
        )
        OrderPrice(
            modifier = Modifier
                .fillMaxWidth(),
            price = order.totalCost,
            cancelOrder = cancelOrder
        )
    }
}

@Composable
private fun OrderHeader(
    modifier: Modifier = Modifier,
    orderId: OrderId,
    status: Title?,
    active: Boolean,
    creationTime: CreationTime
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.order_by_date)
                    .format(creationTime.dateTime.date
                        .toJavaLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                    ),
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier,
                text = orderId.uuid.toString(),
                color = Color.Gray,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.Top)
                .background(
                    color = if (active) Color.Green else Color.Gray,
                    shape = RoundedCornerShape(bottomStart = 15.dp)
                )
                .padding(5.dp)
                .weight(1f),
            text = status?.string ?: stringResource(id = R.string.empty_status),
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Devices(
    modifier: Modifier = Modifier,
    list: List<OrderItemShortModel>
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.products),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        list.forEach {
            val text by remember(it.amount, it.title) {
                derivedStateOf { "- " + it.title.string}
            }
            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = text,
                color = Color.Gray,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
private fun OrderPrice(
    modifier: Modifier = Modifier,
    price: Price,
    cancelOrder: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Bottom)
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(topEnd = 15.dp)
                )
                .clickable(onClick = cancelOrder)
                .padding(5.dp),
            text = stringResource(id = R.string.cancel_order),
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .padding(5.dp),
            text = "${price.bigDecimal} ₽",
            color = Color.Gray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}