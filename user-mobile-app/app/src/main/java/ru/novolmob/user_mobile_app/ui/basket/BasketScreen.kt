@file:OptIn(ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.ui.basket

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.getViewModel
import ru.novolmob.user_mobile_app.R
import ru.novolmob.user_mobile_app.models.DeviceModel
import ru.novolmob.user_mobile_app.services.BasketServiceImpl


@Preview
@Composable
fun BasketScreenPreview() {
    BasketScreen(
        viewModel = BasketViewModel(
            BasketServiceImpl()
        )
    )
}

@Composable
fun BasketScreen(
    modifier: Modifier = Modifier,
    viewModel: BasketViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val state by viewModel.state.collectAsState()
    val basketIsEmpty by remember(state.list.size) {
        derivedStateOf { state.list.isNotEmpty() }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 15.dp),
            contentPadding = PaddingValues(bottom = 130.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(state.list) { device ->
                CatalogItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    device = device,
                    setAmountInBasket = { viewModel.setDeviceAmount(device.deviceId, it) },
                    deleteFromBasket = { viewModel.deleteFromBasket(device.deviceId) }
                )
            }
        }
        if (basketIsEmpty) {
            ConfirmOrderButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 70.dp)
                    .height(60.dp),
                totalPrice = state.totalPriceString,
                onClick = viewModel::confirmOrder
            )
        } else {
            Text(
                modifier = modifier
                    .align(Alignment.Center),
                text = stringResource(id = R.string.basket_is_empty),
                color = Color.LightGray,
                maxLines = 1,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ConfirmOrderButton(
    modifier: Modifier = Modifier,
    totalPrice: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = Color.Green, shape = RoundedCornerShape(100, 100, 0, 0))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = totalPrice,
            fontSize = 22.sp,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(id = R.string.confirm_order),
            fontSize = 16.sp,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CatalogItem(
    modifier: Modifier = Modifier,
    device: DeviceModel,
    setAmountInBasket: (Int) -> Unit,
    deleteFromBasket: () -> Unit
) {
    val amountInBasket by device.amountInBasketFlow.collectAsState()

    val empty = ImageBitmap.imageResource(id = R.drawable.empty)
    val image by device.imageFlow.collectAsState()

    Row(
        modifier = modifier
            .background(color = Color.Gray, shape = RoundedCornerShape(15.dp))
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(shape = RoundedCornerShape(15.dp)),
            bitmap = image ?: empty,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(
                    text = device.title,
                    fontSize = 14.sp,
                    color = Color.White,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = device.description,
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 12.sp
                )
            }
            Row(
                modifier = Modifier
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = device.priceString,
                    maxLines = 1,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = modifier.weight(1f))
                AmountInBasketSetter(
                    modifier = Modifier,
                    amount = amountInBasket,
                    setAmountInBasket = setAmountInBasket
                )
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = deleteFromBasket
                        ),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun AmountInBasketSetter(
    modifier: Modifier = Modifier,
    amount: Int,
    setAmountInBasket: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .background(color = Color.LightGray)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    setAmountInBasket(amount - 1)
                },
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = null,
            tint = Color.Black
        )
        Text(
            modifier = Modifier
                .widthIn(min = 20.dp)
                .background(color = Color.White)
                .padding(horizontal = 2.dp),
            text = amount.toString(),
            maxLines = 1,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
        Icon(
            modifier = Modifier
                .background(color = Color.LightGray)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    setAmountInBasket(amount + 1)
                },
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Black
        )
    }
}