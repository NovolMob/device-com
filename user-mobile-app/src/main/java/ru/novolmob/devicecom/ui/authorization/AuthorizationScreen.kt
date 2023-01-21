@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package ru.novolmob.devicecom.ui.authorization

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import ru.novolmob.devicecom.R
import ru.novolmob.devicecom.mutablevalue.MutableValue
import ru.novolmob.devicecom.navigation.NavigationRoute.Registration.navigateToRegistration
import ru.novolmob.devicecom.utils.PhoneNumberVisualTransformation

@Preview(showBackground = true)
@Composable
fun AuthorizationScreenPreview(

) {
    AuthorizationScreen()
}

@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthorizationViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()
    val focusManager = LocalFocusManager.current

    BackHandler {}

    LaunchedEffect(key1 = true) {
        launch {
            snapshotFlow { pagerState.currentPage }.collectLatest {
                viewModel.reset()
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(350.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        if (!state.loading) navHostController.navigateToRegistration()
                    },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.registration),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.LightGray
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            Column(
                modifier = Modifier
                    .height(350.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthTypesRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    pagerState = pagerState
                )
                Form(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    pagerState = pagerState,
                    enabled = !state.loading,
                    emailState = state.email,
                    phoneNumberState = state.phoneNumber,
                    passwordState = state.password,
                    loginByEmail = viewModel::loginByEmail,
                    loginByPhoneNumber = viewModel::loginByPhoneNumber
                )
                LoginButton(
                    modifier = Modifier
                        .padding(vertical = 5.dp),
                    enabled = state.canEnter,
                    loading = state.loading,
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.login()
                    }
                )
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = stringResource(id = R.string.authorization),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 88.dp, top = 35.dp),
            text = "by NovolMob",
            fontSize = 10.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
private fun AuthTypesRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SelectingTabForPagerState(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.by_email),
            number = 0,
            pagerState = pagerState
        )
        SelectingTabForPagerState(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.by_phone_number),
            number = 1,
            pagerState = pagerState
        )
    }
}

@Composable
private fun Form(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    pagerState: PagerState,
    emailState: MutableValue<String>,
    phoneNumberState: MutableValue<String>,
    passwordState: MutableValue<String>,
    loginByEmail: () -> Unit,
    loginByPhoneNumber: () -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        count = 2,
        state = pagerState,
        itemSpacing = 30.dp
    ) { page ->
        if (page == 0) {
            AuthForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                enabled = enabled,
                loginState = emailState,
                loginPlaceholder = stringResource(id = R.string.email),
                passwordState = passwordState,
                onDone = loginByEmail,
                loginOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )
        } else {
            AuthForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                enabled = enabled,
                loginState = phoneNumberState,
                loginPlaceholder = stringResource(id = R.string.phone_number),
                passwordState = passwordState,
                onDone = loginByPhoneNumber,
                loginOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                loginVisualTransformation = PhoneNumberVisualTransformation
            )
        }
    }
}

@Composable
private fun SelectingTabForPagerState(
    modifier: Modifier = Modifier,
    text: String,
    number: Int,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    val isSelected by remember(number, pagerState.currentPage) {
        derivedStateOf { pagerState.currentPage == number }
    }
    val color by remember(isSelected) {
        derivedStateOf { if (isSelected) Color.Gray else Color.LightGray }
    }
    val dividerWidth by remember(isSelected) {
        derivedStateOf { if (isSelected) 0.9f else 0f }
    }
    val colorAnimation by animateColorAsState(targetValue = color)
    val dividerWidthAnimation by animateFloatAsState(targetValue = dividerWidth)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    scope.launch { pagerState.animateScrollToPage(number) }
                },
            text = text,
            fontSize = 16.sp,
            color = colorAnimation,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(dividerWidthAnimation)
                .height(1.dp)
                .background(color = Color.Black)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@Composable
private fun AuthForm(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    loginState: MutableValue<String>,
    loginPlaceholder: String,
    loginOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    loginVisualTransformation: VisualTransformation = VisualTransformation.None,
    passwordState: MutableValue<String>,
    onDone: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            valueState = loginState,
            placeholder = loginPlaceholder,
            keyboardOptions = loginOptions,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            visualTransformation = loginVisualTransformation
        )

        PasswordField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            passwordState = passwordState
        ) {
            focusManager.clearFocus()
            onDone()
        }
    }
}

@Composable
private fun PasswordField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    passwordState: MutableValue<String>,
    onDone: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val lockColor by remember(passwordVisible) {
        derivedStateOf { if (passwordVisible) Color.Gray else Color.Blue.copy(alpha = 0.4f) }
    }

    Box(modifier = modifier) {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            valueState = passwordState,
            placeholder = stringResource(id = R.string.password),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(
                onDone = { onDone() }
            ),
            visualTransformation = {
                if (!passwordVisible)
                    TransformedText(
                        AnnotatedString("•".repeat(it.text.length)),
                        OffsetMapping.Identity
                    )
                else TransformedText(it, OffsetMapping.Identity)
            }
        )
        Icon(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .size(17.dp)
                .align(Alignment.CenterEnd)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    passwordVisible = !passwordVisible
                },
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = lockColor
        )
    }
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    val textWidth by remember(enabled) {
        derivedStateOf { if (enabled) 0.8f else 0.4f }
    }
    val textHeight by remember(enabled) {
        derivedStateOf { if (enabled) 40.dp else 30.dp }
    }
    val textBackgroundColor by remember(enabled) {
        derivedStateOf { if (enabled) Color.Green else Color.LightGray }
    }
    val textWidthAnimation by animateFloatAsState(targetValue = textWidth)
    val textHeightAnimation by animateDpAsState(targetValue = textHeight)
    val textBackgroundColorAnimation by animateColorAsState(targetValue = textBackgroundColor)

    Box(
        modifier = modifier
            .fillMaxWidth(textWidthAnimation)
            .height(textHeightAnimation)
            .background(color = textBackgroundColorAnimation, shape = CircleShape)
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                if (enabled) onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(25.dp),
                color = Color.White
            )
        } else {
            Text(
                modifier = Modifier,
                text = if (enabled) stringResource(id = R.string.login) else "",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    valueState: MutableValue<String>,
    placeholder: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val value by valueState.value.collectAsState()
    val isValid by valueState.valid.collectAsState()

    val color by remember(value) {
        derivedStateOf {
            if (value.isNotEmpty())
                if (isValid) Color.Green else Color.Red
            else Color.LightGray
        }
    }

    val colorAnimation by animateColorAsState(targetValue = color)

    BasicTextField(
        modifier = modifier
            .border(width = 1.dp, color = colorAnimation, shape = CircleShape)
            .background(color = colorAnimation.copy(alpha = 0.04f), shape = CircleShape),
        value = value,
        enabled = enabled,
        onValueChange = {
            valueState.set(it)
        },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        ),
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        decorationBox = {
            Box(modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp), contentAlignment = Alignment.Center) {
                if (value.isEmpty())
                    Text(
                        text = placeholder,
                        color = Color.LightGray,
                        fontSize = 18.sp
                    )
                it()
            }
        }
    )
}