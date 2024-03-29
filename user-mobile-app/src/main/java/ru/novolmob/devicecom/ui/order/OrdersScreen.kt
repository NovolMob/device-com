@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)

package ru.novolmob.devicecom.ui.order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.datetime.toJavaLocalDate
import org.koin.androidx.compose.getViewModel
import ru.novolmob.backendapi.models.OrderItemShortModel
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.core.models.CreationTime
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.devicecom.R
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullRefreshState(refreshing = state.loading, onRefresh = viewModel::update)
    var selectedOrderForCancellation by remember {
        mutableStateOf<OrderShortModel?>(null)
    }
    val hiddenAlertDialog by remember(selectedOrderForCancellation) {
        derivedStateOf { selectedOrderForCancellation == null }
    }

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
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
            Column(
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {}
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 65.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(
                    items = state.orders,
                    key = { it.id.uuid.javaUUID }
                ) {
                    Order(
                        modifier = Modifier
                            .fillMaxWidth(),
                        language = state.language,
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
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter),
            refreshing = state.loading,
            state = pullRefreshState
        )
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
                        .clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = onDismiss
                        ),
                    text = stringResource(id = R.string.cancel),
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = onConfirm
                        ),
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

@Composable
private fun Order(
    modifier: Modifier = Modifier,
    language: Locale,
    order: OrderShortModel,
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
            language = language,
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
    language: Locale,
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
            val date by remember(creationTime, language) {
                derivedStateOf {
                    creationTime.dateTime.date
                        .toJavaLocalDate()
                        .format(
                            DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy", language
                            )
                        )
                }
            }
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.order_by_date)
                    .format(date),
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier,
                text = orderId.toString(),
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
            text = "${price.bigDecimal.javaBigDecimal} ₽",
            color = Color.Gray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}