package ru.novolmob.user_mobile_app.ui.device

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import ru.novolmob.user_mobile_app.R

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    viewModel: DeviceViewModel = getViewModel(),
    navHostController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    val isLoaded by remember(state.deviceId) {
        derivedStateOf { state.deviceId != null }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        if (!isLoaded) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                color = Color.LightGray
            )
        } else {
            Column(
                modifier = modifier
                    .background(color = Color.White)
                    .padding(15.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    imageFlow = state.imageFlow
                )
                Title(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = state.title,
                    article = state.article
                )
                Description(
                    modifier = Modifier
                        .fillMaxWidth(),
                    description = state.description
                )
                Features(
                    modifier = Modifier
                        .fillMaxWidth(),
                    type = state.type,
                    features = state.features
                )
            }
            BottomButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                totalPrice = state.priceString,
                amountInBasketFlow = state.amountInBasketFlow,
                addToBasket = viewModel::addToBasket,
                setAmountInBasket = viewModel::setAmountInBasket,
                deleteFromBasket = viewModel::deleteFromBasket
            )
        }
    }
}

@Composable
private fun Description(
    modifier: Modifier = Modifier,
    description: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.description),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
            text = description,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun Features(
    modifier: Modifier = Modifier,
    type: String,
    features: Map<String, String>,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.features),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        FeatureCell(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            title = stringResource(id = R.string.device_type),
            value = type
        )
        features.forEach { (title, value) ->
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.LightGray
            )
            FeatureCell(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
                    .padding(vertical = 3.dp),
                title = title,
                value = value
            )
        }
    }
}

@Composable
private fun FeatureCell(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            fontSize = 16.sp,
            color = Color.Gray
        )
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier,
                text = value,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Image(
    modifier: Modifier = Modifier,
    imageFlow: StateFlow<ImageBitmap?>
) {
    val empty = ImageBitmap.imageResource(id = R.drawable.image1)
    val image by imageFlow.collectAsState(empty)
    androidx.compose.foundation.Image(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
        bitmap = image ?: empty,
        contentDescription = null
    )
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    title: String,
    article: String
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = article,
            color = Color.LightGray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun BottomButton(
    modifier: Modifier = Modifier,
    totalPrice: String,
    amountInBasketFlow: StateFlow<Int>,
    addToBasket: () -> Unit,
    setAmountInBasket: (Int) -> Unit,
    deleteFromBasket: () -> Unit
) {
    val amountInBasket by amountInBasketFlow.collectAsState()
    val containInBasket by remember(amountInBasket) {
        derivedStateOf { amountInBasket > 0 }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 70.dp)
    ) {
        if (containInBasket) {
            Row(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp)
                        .clickable(
                            MutableInteractionSource(),
                            indication = null,
                            onClick = {}
                        ),
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Gray
                )
                AmountInBasket(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(shape = RoundedCornerShape(100, 100, 0, 0))
                        .fillMaxWidth(0.5f),
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
                    tint = Color.Gray
                )
            }
        } else {
            AddToBasketButton(
                modifier = Modifier
                    .height(60.dp)
                    .clip(shape = RoundedCornerShape(100, 100, 0, 0))
                    .fillMaxSize(),
                totalPrice = totalPrice,
                onClick = addToBasket
            )
        }
    }

}

@Composable
private fun AmountInBasket(
    modifier: Modifier = Modifier,
    amount: Int,
    setAmountInBasket: (Int) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
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
                .weight(1f)
                .background(color = Color.White)
                .padding(horizontal = 2.dp),
            text = amount.toString(),
            maxLines = 1,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
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

@Composable
private fun AddToBasketButton(
    modifier: Modifier = Modifier,
    totalPrice: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = Color.Green)
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
            text = stringResource(id = R.string.add_to_basket_full),
            fontSize = 16.sp,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
