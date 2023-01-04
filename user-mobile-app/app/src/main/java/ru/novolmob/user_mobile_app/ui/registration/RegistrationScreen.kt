@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.ui.registration

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import org.koin.androidx.compose.getViewModel
import ru.novolmob.core.extensions.LocalDateTimeExtension.now
import ru.novolmob.user_mobile_app.R
import ru.novolmob.user_mobile_app.mutablevalue.MutableValue
import ru.novolmob.user_mobile_app.mutablevalue.PasswordMutableValue
import ru.novolmob.user_mobile_app.utils.PhoneNumberVisualTransformation
import java.time.format.DateTimeFormatter
import java.util.*

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen()
}

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsState()

    BackHandler {}

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
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
                        if (!state.loading) navHostController.popBackStack()
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.LightGray
                )
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.authorization),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.LightGray
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                BodyPager(
                    {
                        FioForm(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            enabled = !state.loading,
                            firstnameState = state.firstname,
                            lastnameState = state.lastname,
                            patronymicState = state.patronymic
                        ) {
                            focusManager.clearFocus()
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    },
                    {
                        SecondForm(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            enabled = !state.loading,
                            birthdayState = state.birthday,
                            cityState = state.city,
                            allCities = state.availableCities,
                            languageState = state.language,
                            allLanguages = state.availableLanguages,
                        )
                    },
                    {
                        CredentialsForm(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            enabled = !state.loading,
                            emailState = state.email,
                            phoneNumberState = state.phoneNumber,
                            passwordState = state.password,
                            onDone = viewModel::register
                        )
                    },
                    modifier = Modifier.weight(1f),
                    pagerState = pagerState
                )
                SigninButton(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    enabled = state.canSend,
                    loading = state.loading,
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.register()
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
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.registration),
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
private fun BodyPager(
    vararg blocks: @Composable () -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val segmentMinSize = 5.dp
    val segmentMaxWidth by remember(segmentMinSize) {
        derivedStateOf { segmentMinSize * 4 }
    }
    Column(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = pagerState,
            count = blocks.size,
        ) { page ->
            blocks[page]()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(blocks.size) {
                val isSelected by remember(it, pagerState.currentPage) {
                    derivedStateOf { it == pagerState.currentPage }
                }
                val color by remember(isSelected) {
                    derivedStateOf { if (isSelected) Color.Gray else Color.LightGray }
                }
                val width by remember(isSelected) {
                    derivedStateOf { if (isSelected) segmentMaxWidth else segmentMinSize }
                }
                val colorAnimation by animateColorAsState(targetValue = color)
                val widthAnimation by animateDpAsState(targetValue = width)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                        .height(segmentMinSize)
                        .width(widthAnimation)
                        .background(color = colorAnimation, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
private fun FioForm(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    firstnameState: MutableValue<String>,
    lastnameState: MutableValue<String>,
    patronymicState: MutableValue<String>,
    onNext: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            valueState = firstnameState,
            title = stringResource(id = R.string.firstname),
            required = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            valueState = lastnameState,
            title = stringResource(id = R.string.lastname),
            required = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            valueState = patronymicState,
            title = stringResource(id = R.string.patronymic),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { onNext() })
        )
    }
}

@Composable
private fun SecondForm(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    birthdayState: MutableValue<LocalDate?>,
    allCities: List<String>,
    cityState: MutableValue<String>,
    allLanguages: List<Locale>,
    languageState: MutableValue<String>
) {
    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        BirthdayPicker(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            birthdayState = birthdayState
        )
        CityField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            cities = allCities,
            cityState = cityState
        )
        LanguageField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            languages = allLanguages,
            languageState = languageState
        )
    }
}

@Composable
private fun CredentialsForm(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    emailState: MutableValue<String>,
    phoneNumberState: MutableValue<String>,
    passwordState: PasswordMutableValue,
    onDone: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            valueState = emailState,
            title = stringResource(id = R.string.email),
            placeholder = "example@gmail.com",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )

        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            valueState = phoneNumberState,
            required = true,
            title = stringResource(id = R.string.phone_number),
            placeholder = "+7 999 765 43 21",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
            visualTransformation = PhoneNumberVisualTransformation
        )
        PasswordField(
            enabled = enabled,
            title = stringResource(id = R.string.password),
            passwordState = passwordState.firstState,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        PasswordField(
            enabled = enabled,
            title = stringResource(id = R.string.repeat_password),
            passwordState = passwordState.secondState,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onDone()
            })
        )
    }
}

@Composable
private fun SigninButton(
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
                text = if (enabled) stringResource(id = R.string.signin) else "",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CityField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    cities: List<String>,
    cityState: MutableValue<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val city by cityState.value.collectAsState()

    val textColor by remember(city) {
        derivedStateOf {
            if (city.isNotEmpty())
                Color.Black
            else Color.LightGray
        }
    }

    FieldStyle(modifier = modifier, title = stringResource(id = R.string.city)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                .background(
                    color = Color.LightGray.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    if (enabled) expanded = true
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = city,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 5.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    if (enabled) cityState.set("")
                },
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = textColor
        )
    }
    DropdownMenu(
        modifier = Modifier
            .fillMaxWidth(),
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }
    ) {
        cities.forEach {
            val isSelected by remember(city, it) {
                derivedStateOf { city == it }
            }
            val color by remember {
                derivedStateOf { if (isSelected) Color.Black else Color.LightGray }
            }
            DropdownMenuItem(
                onClick = {
                    if (enabled) cityState.set(it)
                    expanded = false
                }
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = it,
                    color = color,
                    maxLines = 1,
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    languages: List<Locale>,
    languageState: MutableValue<String>
) {
    val language by languageState.value.collectAsState()

    var expanded by remember {
        mutableStateOf(false)
    }
    FieldStyle(modifier = modifier, title = stringResource(id = R.string.language), required = true) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                .background(
                    color = Color.LightGray.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    if (enabled) expanded = true
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = language,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
    DropdownMenu(
        modifier = Modifier
            .fillMaxWidth(),
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }
    ) {
        languages.forEach {
            val isSelected by remember(language, it) {
                derivedStateOf { language == it.language }
            }
            val color by remember {
                derivedStateOf { if (isSelected) Color.Black else Color.LightGray }
            }
            DropdownMenuItem(
                onClick = {
                    if (enabled) languageState.set(it.language)
                    expanded = false
                }
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = it.displayCountry,
                    color = color,
                    maxLines = 1,
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun BirthdayPicker(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    birthdayState: MutableValue<LocalDate?>
) {
    val date by birthdayState.value.collectAsState()
    val isValid by birthdayState.valid.collectAsState()

    val context = LocalContext.current
    val datePickerDialog by remember(date) {
        derivedStateOf {
            (date ?: LocalDate.now()).let {
                DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, day: Int ->
                        if (enabled) LocalDate(year, month + 1, day).let(birthdayState::set)
                    }, it.year, it.monthNumber - 1, it.dayOfMonth
                )
            }
        }
    }

    val color by remember(isValid) {
        derivedStateOf {
            if (date != null)
                if (isValid) Color.Green else Color.Red
            else Color.LightGray
        }
    }
    val colorAnimation by animateColorAsState(targetValue = color)

    val textColor by remember(date) {
        derivedStateOf {
            if (date != null) Color.Black else Color.LightGray
        }
    }

    FieldStyle(modifier = modifier, title = stringResource(id = R.string.birthday)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = colorAnimation, shape = CircleShape)
                .background(
                    color = colorAnimation.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    if (enabled) datePickerDialog.show()
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = date?.toJavaLocalDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 5.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    if (enabled) birthdayState.set(null)
                },
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = textColor
        )
    }
}

@Composable
private fun PasswordField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    passwordState: MutableValue<String>,
    title: String,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var passwordVisible by rememberSaveable(inputs = arrayOf()) { mutableStateOf(false) }
    val lockColor by remember(passwordVisible) {
        derivedStateOf { if (passwordVisible) Color.Gray else Color.Blue.copy(alpha = 0.4f) }
    }

    Box(modifier = modifier) {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            required = true,
            title = title,
            valueState = passwordState,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
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
private fun InputField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    valueState: MutableValue<String>,
    required: Boolean = false,
    title: String,
    placeholder: String = "",
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

    FieldStyle(modifier = modifier, title = title, required = required) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = colorAnimation, shape = CircleShape)
                .background(
                    color = colorAnimation.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(15.dp)
                ),
            enabled = enabled,
            value = value,
            onValueChange = valueState::set,
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
}

@Composable
private fun FieldStyle(
    modifier: Modifier = Modifier,
    title: String,
    required: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp),
            content = content
        )
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 30.dp)
                .background(color = Color.White)
                .padding(horizontal = 5.dp),
        ) {
            Text(
                modifier = Modifier,
                text = title,
                fontSize = 16.sp,
                color = Color.Black.copy(alpha = 0.6f)
            )
            if (required) {
                Text(
                    modifier = Modifier,
                    text = "*",
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }
        }
    }
}
