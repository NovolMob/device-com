@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.ui.profile

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
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
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = getViewModel(),
    navHostController: NavHostController = rememberAnimatedNavController()
) {
    val focusManager = LocalFocusManager.current
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()

    BackHandler {}

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = viewModel::logout
                    ),
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Avatar(
            modifier = Modifier
                .size(150.dp),
            image = ImageBitmap.imageResource(id = R.drawable.empty),
            initials = state.initials
        )
        BodyPager(
            {
                FioForm(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    firstnameState = state.firstname,
                    lastnameState = state.lastname,
                    patronymicState = state.patronymic,
                ) {
                    focusManager.clearFocus()
                    scope.launch { pagerState.animateScrollToPage(1) }
                }
            },
            {
                SecondForm(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    birthdayState = state.birthday,
                    allCities = state.availableCities,
                    cityState = state.city,
                    allLanguages = state.availableLanguages,
                    languageState = state.language
                )
            },
            {
                CredentialsForm(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    emailState = state.email,
                    phoneNumberState = state.phoneNumber,
                    passwordState = state.password,
                    onDone = viewModel::saveProfile
                )
            },
            modifier = Modifier.height(350.dp),
            pagerState = pagerState
        )
        SaveButton(
            modifier = Modifier
                .padding(vertical = 15.dp),
            enable = state.canSave,
            loading = state.loading,
            onClick = viewModel::saveProfile
        )
    }
}

@Composable
private fun Avatar(
    modifier: Modifier = Modifier,
    image: ImageBitmap?,
    initials: String
) {
    val containImage by remember(image) {
        derivedStateOf { image != null }
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(color = Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (containImage) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                bitmap = image!!,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = initials,
                color = Color.Gray,
                fontSize = 55.sp
            )
        }
    }
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
            valueState = firstnameState,
            title = stringResource(id = R.string.firstname),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            valueState = lastnameState,
            title = stringResource(id = R.string.lastname),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
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
    birthdayState: MutableValue<LocalDate?>,
    allCities: List<String>,
    cityState: MutableValue<String>,
    allLanguages: List<Locale>,
    languageState: MutableValue<String>,
) {
    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        BirthdayPicker(
            modifier = Modifier
                .fillMaxWidth(),
            birthdayState = birthdayState
        )
        CityField(
            modifier = Modifier
                .fillMaxWidth(),
            cities = allCities,
            cityState = cityState
        )
        LanguageField(
            modifier = Modifier
                .fillMaxWidth(),
            languages = allLanguages,
            languageState = languageState
        )
    }
}

@Composable
private fun CredentialsForm(
    modifier: Modifier = Modifier,
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
            valueState = emailState,
            title = stringResource(id = R.string.email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            valueState = phoneNumberState,
            title = stringResource(id = R.string.phone_number),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
            visualTransformation = PhoneNumberVisualTransformation
        )
        PasswordField(
            title = stringResource(id = R.string.password),
            passwordState = passwordState.firstState,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        PasswordField(
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
private fun SaveButton(
    modifier: Modifier = Modifier,
    enable: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    val textWidth by remember(enable) {
        derivedStateOf { if (enable) 0.8f else 0.4f }
    }
    val textHeight by remember(enable) {
        derivedStateOf { if (enable) 40.dp else 30.dp }
    }
    val textBackgroundColor by remember(enable) {
        derivedStateOf { if (enable) Color.Green else Color.LightGray }
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
                if (enable) onClick()
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
                text = if (enable) stringResource(id = R.string.save_profile) else "",
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CityField(
    modifier: Modifier = Modifier,
    cities: List<String>,
    cityState: MutableValue<String>
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val city by cityState.value.collectAsState()
    val textColor by remember(city) {
        derivedStateOf {
            if (city.isNotEmpty()) Color.Black
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
                    expanded = true
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = city,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
        if (city.isNotEmpty()) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        cityState.set("")
                    },
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = textColor
            )
        }
    }
    DropdownMenu(
        expanded = expanded,
        toString = String::toString,
        list = cities,
        selected = city,
        onDismissRequest = {
            expanded = false
        }
    ) {
        cityState.set(it)
        expanded = false
    }
}

@Composable
private fun LanguageField(
    modifier: Modifier = Modifier,
    languages: List<Locale>,
    languageState: MutableValue<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val language by languageState.value.collectAsState()
    val textColor by remember(language) {
        derivedStateOf {
            if (language.isNotEmpty()) Color.Black
            else Color.LightGray
        }
    }
    FieldStyle(modifier = modifier, title = stringResource(id = R.string.language)) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.LightGray, shape = CircleShape)
                .background(
                    color = Color.LightGray.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(15.dp)
                )
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    expanded = true
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = language,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
    DropdownMenu(
        expanded = expanded,
        toString = { it.language },
        list = languages,
        selected = language,
        onDismissRequest = {
            expanded = false
        }
    ) {
        languageState.set(it.language)
        expanded = false
    }
}

@Composable
private fun <T> DropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    toString: (T) -> String,
    list: List<T>,
    selected: String,
    onDismissRequest: () -> Unit = {},
    onClick: (T) -> Unit
) {
    DropdownMenu(
        modifier = modifier
            .fillMaxWidth(),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        list.forEach {
            val isSelected by remember(selected, it) {
                derivedStateOf { selected == toString(it) }
            }
            val color by remember {
                derivedStateOf { if (isSelected) Color.Black else Color.LightGray }
            }
            DropdownMenuItem(
                onClick = {
                    onClick(it)
                }
            ) {
                val text by remember(it, toString) {
                    derivedStateOf { toString(it) }
                }
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = text,
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
    birthdayState: MutableValue<LocalDate?>,
) {
    val context = LocalContext.current
    val date by birthdayState.value.collectAsState()

    val datePickerDialog by remember(date) {
        derivedStateOf {
            (date ?: LocalDate.now()).let {
                DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, day: Int ->
                        LocalDate(year, month + 1, day).let(birthdayState::set)
                    }, it.year, it.monthNumber - 1, it.dayOfMonth
                )
            }
        }
    }
    val isValid by birthdayState.valid.collectAsState()

    val color by remember(date) {
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
                    datePickerDialog.show()
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = date?.toJavaLocalDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
        if (date != null) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
                    .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                        birthdayState.set(null)
                    },
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = textColor
            )
        }
    }
}

@Composable
private fun PasswordField(
    modifier: Modifier = Modifier,
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
    valueState: MutableValue<String>,
    title: String,
    placeholder: String? = null,
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

    FieldStyle(modifier = modifier, title = title) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = colorAnimation, shape = CircleShape)
                .background(
                    color = colorAnimation.copy(alpha = 0.04f),
                    shape = RoundedCornerShape(15.dp)
                ),
            value = value,
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
                    if (placeholder != null && value.isEmpty())
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
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp),
            content = content
        )
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 30.dp)
                .background(color = Color.White)
                .padding(horizontal = 5.dp),
            text = title,
            fontSize = 16.sp,
            color = Color.Black.copy(alpha = 0.6f)
        )
    }
}