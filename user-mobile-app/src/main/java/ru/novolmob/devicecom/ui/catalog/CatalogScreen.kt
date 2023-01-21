@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)

package ru.novolmob.devicecom.ui.catalog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.getViewModel
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.devicecom.R
import ru.novolmob.devicecom.models.DeviceModel
import ru.novolmob.devicecom.navigation.NavigationRoute.Main.Catalog.Device.navigateToDevice

@Preview
@Composable
fun CatalogScreenPreview() {
    CatalogScreen()
}

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    viewModel: CatalogViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullRefreshState(refreshing = state.searching, onRefresh = viewModel::search)

    BackHandler {}

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .pullRefresh(pullRefreshState)
    ) {
        Column(
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {}
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                startValue = state.searchString,
                update = viewModel::searchString,
                onDone = viewModel::search
            )
            AnimatedVisibility(
                visible = !state.searching,
                enter = expandVertically(tween(200)),
                exit = shrinkVertically(tween(200))
            ) {
                CatalogContent(
                    modifier = Modifier
                        .fillMaxWidth(),
                    catalog = state.catalog,
                    amountOfPages = state.amountOfPage,
                    selectedPage = state.page,
                    setPage = viewModel::selectedPage,
                    setAmountInBasket = viewModel::setDeviceAmount,
                    addToBasket = viewModel::addToBasket,
                    openDevice = {
                        viewModel.openDevice(it)
                        navHostController.navigateToDevice()
                    }
                )
            }
        }
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter),
            refreshing = state.searching,
            state = pullRefreshState
        )
    }
}

@Composable
private fun CatalogContent(
    modifier: Modifier = Modifier,
    catalog: List<DeviceModel>,
    amountOfPages: Int,
    selectedPage: Int,
    setAmountInBasket: (DeviceId, Int) -> Unit,
    addToBasket: (DeviceModel) -> Unit,
    setPage: (Int) -> Unit,
    openDevice: (DeviceId) -> Unit
) {
    val hasPages by remember(amountOfPages) {
        derivedStateOf { amountOfPages > 1 }
    }
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(top = if (hasPages) 70.dp else 0.dp, bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(catalog) { device ->
                CatalogItem(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .height(120.dp),
                    device = device,
                    setAmountInBasket = { setAmountInBasket(device.id, it) },
                    addToBasket = { addToBasket(device) },
                    onClick = { openDevice(device.id) }
                )
            }
        }
        if (hasPages) {
            PageSelecting(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .height(50.dp),
                amountOfPages = amountOfPages,
                selected = selectedPage,
                setPage = setPage
            )
        }
    }
}

@Composable
private fun CatalogItem(
    modifier: Modifier = Modifier,
    device: DeviceModel,
    setAmountInBasket: (Int) -> Unit,
    addToBasket: () -> Unit,
    onClick: () -> Unit
) {
    val amountInBasket by device.amountInBasketFlow.collectAsState()
    val containInBasket by remember(amountInBasket) {
        derivedStateOf { amountInBasket > 0  }
    }

    val empty = ImageBitmap.imageResource(id = R.drawable.empty)
    val image by device.imageFlow.collectAsState()
    
    Row(
        modifier = modifier
            .background(color = Color.Gray, shape = RoundedCornerShape(15.dp))
            .padding(5.dp)
            .clickable(onClick = onClick),
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
                    .height(30.dp)
                    .padding(1.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Spacer(modifier = modifier.weight(1f))
                if (containInBasket) {
                    AmountInBasketSetter(
                        modifier = Modifier,
                        amount = amountInBasket,
                        setAmountInBasket = setAmountInBasket
                    )
                } else {
                    AddToBasketButton(
                        modifier = Modifier,
                        priceString = device.priceString,
                        onClick = addToBasket
                    )
                }
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

@Composable
private fun AddToBasketButton(
    modifier: Modifier = Modifier,
    priceString: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .background(color = Color.Green, shape = RoundedCornerShape(15.dp))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier,
            imageVector = Icons.Rounded.ShoppingCart,
            contentDescription = null,
            tint = Color.Black
        )
        Text(
            text = priceString,
            maxLines = 1,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun PageSelecting(
    modifier: Modifier = Modifier,
    amountOfPages: Int,
    selected: Int,
    setPage: (Int) -> Unit
) {
    LazyRow(
        state = rememberLazyListState(initialFirstVisibleItemIndex = 0),
        modifier = modifier
            .background(color = Color.LightGray.copy(alpha = 0.9f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(
            count = amountOfPages,
            key = { it }
        ) {
            val isSelected by remember(selected) {
                derivedStateOf { it == selected }
            }
            PageItem(selected = isSelected, pageTitle = (it + 1).toString()) {
                setPage(it)
            }
        }
    }
}

@Composable
private fun PageItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    pageTitle: String,
    onClick: () -> Unit
) {
    val textColor by remember(selected) {
        derivedStateOf { if (selected) Color.White else Color.Gray }
    }
    val backgroundColor by remember(selected) {
        derivedStateOf { if (selected) Color.Black else Color.White }
    }
    Box(
        modifier = modifier
            .padding(7.dp)
            .widthIn(min = 30.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(15))
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pageTitle,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun SearchRow(
    modifier: Modifier = Modifier,
    startValue: String = "",
    update: (String) -> Unit,
    onDone: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember {
        mutableStateOf(startValue)
    }
    var searchActive by remember {
        mutableStateOf(false)
    }
    val searchBackground by remember(searchActive) {
        derivedStateOf{
            if (searchActive) Color.LightGray.copy(0.4f) else Color.LightGray
        }
    }

    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                searchActive = it.isFocused
            }
            .background(color = searchBackground, shape = RoundedCornerShape(10, 10, 100, 100)),
        value = value,
        onValueChange = {
            value = it
            update(it)
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDone()
            }
        ),
        decorationBox = {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp), contentAlignment = Alignment.TopStart) {
                if (value.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = stringResource(id = R.string.search),
                        color = Color.Black.copy(alpha = 0.5f),
                        fontSize = 18.sp
                    )
                    it()
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            it()
                        }
                        Icon(
                            modifier = Modifier
                                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                                    focusManager.clearFocus()
                                    onDone()
                                },
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    )
}