@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.ui.registration

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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
import org.koin.androidx.compose.getViewModel
import ru.novolmob.user_mobile_app.R
import ru.novolmob.user_mobile_app.services.ProfileServiceImpl
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Preview
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(
        viewModel = RegistrationViewModel(
            ProfileServiceImpl()
        )
    )
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
                        navHostController.popBackStack()
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
                            setFirstname = viewModel::firstname,
                            setLastname = viewModel::lastname,
                            setPatronymic = viewModel::patronymic
                        ) { scope.launch { pagerState.animateScrollToPage(1) } }
                    },
                    {
                        SecondForm(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            setBirthday = viewModel::birthday,
                            birthdayValid = viewModel::validBirthday,
                            allCities = state.availableCities,
                            setCity = viewModel::city,
                            allLanguages = state.availableLanguages,
                            setLanguage = viewModel::language
                        )
                    },
                    {
                        CredentialsForm(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            setEmail = viewModel::email,
                            emailValidator = viewModel::validEmail,
                            setPhoneNumber = viewModel::phoneNumber,
                            phoneNumberValidator = viewModel::validPhoneNumber,
                            setPassword = viewModel::password,
                            onDone = viewModel::registration
                        )
                    },
                    modifier = Modifier.weight(1f),
                    pagerState = pagerState
                )
                SigninButton(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    enable = state.canSend,
                    loading = state.loading,
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.registration()
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
    setFirstname: (String) -> Unit,
    setLastname: (String) -> Unit,
    setPatronymic: (String) -> Unit,
    onNext: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            updateValue = setFirstname,
            title = stringResource(id = R.string.firstname),
            required = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            updateValue = setLastname,
            title = stringResource(id = R.string.lastname),
            required = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            updateValue = setPatronymic,
            title = stringResource(id = R.string.patronymic),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { onNext() })
        )
    }
}

@Composable
private fun SecondForm(
    modifier: Modifier = Modifier,
    setBirthday: (LocalDate?) -> Unit,
    birthdayValid: (LocalDate) -> Boolean,
    allCities: List<String>,
    setCity: (String?) -> Unit,
    allLanguages: List<Locale>,
    setLanguage: (Locale) -> Unit,
) {
    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        BirthdayPicker(
            modifier = Modifier
                .fillMaxWidth(),
            validator = birthdayValid,
            setDate = setBirthday
        )
        CityField(
            modifier = Modifier
                .fillMaxWidth(),
            cities = allCities,
            setCity = setCity
        )
        LanguageField(
            modifier = Modifier
                .fillMaxWidth(),
            languages = allLanguages,
            setLanguage = setLanguage
        )
    }
}

@Composable
private fun CredentialsForm(
    modifier: Modifier = Modifier,
    setEmail: (String) -> Unit,
    emailValidator: (String) -> Boolean,
    setPhoneNumber: (String) -> Unit,
    phoneNumberValidator: (String) -> Boolean,
    setPassword: (String) -> Unit,
    onDone: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var firstPassword by rememberSaveable(inputs = arrayOf()) {
        mutableStateOf("")
    }

    Column(modifier = modifier.padding(horizontal = 25.dp)) {
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            validator = emailValidator,
            updateValue = setEmail,
            title = stringResource(id = R.string.email),
            placeholder = "example@gmail.com",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        InputField(
            modifier = Modifier
                .fillMaxWidth(),
            validator = phoneNumberValidator,
            required = true,
            updateValue = setPhoneNumber,
            title = stringResource(id = R.string.phone_number),
            placeholder = "+7 999 765 43 21",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        PasswordField(
            title = stringResource(id = R.string.password),
            setPassword = { firstPassword = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) })
        )
        PasswordField(
            title = stringResource(id = R.string.repeat_password),
            setPassword = { setPassword(if (it == firstPassword) it else "") },
            validator = { it == firstPassword },
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
                text = if (enable) stringResource(id = R.string.signin) else "",
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
    setCity: (String?) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var city by rememberSaveable(inputs = arrayOf()) {
        mutableStateOf<String?>(null)
    }

    val textColor by remember(city) {
        derivedStateOf {
            if (city != null)
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
                    expanded = true
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = city ?: "",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 5.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    setCity(null)
                    city = null
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
            setCity(city)
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
                    setCity(it)
                    city = it
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
    languages: List<Locale>,
    setLanguage: (Locale) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var language by rememberSaveable(inputs = arrayOf()) {
        mutableStateOf(languages.first())
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
                    expanded = true
                }
                .padding(horizontal = 5.dp, vertical = 5.dp),
            text = language.displayCountry,
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
            setLanguage(language)
            expanded = false
        }
    ) {
        languages.forEach {
            val isSelected by remember(language, it) {
                derivedStateOf { language == it }
            }
            val color by remember {
                derivedStateOf { if (isSelected) Color.Black else Color.LightGray }
            }
            DropdownMenuItem(
                onClick = {
                    setLanguage(it)
                    language = it
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
    validator: ((LocalDate) -> Boolean)? = null,
    setDate: (LocalDate?) -> Unit
) {
    val context = LocalContext.current
    var date by rememberSaveable(inputs = arrayOf()) {
        mutableStateOf<LocalDate?>(null)
    }
    val datePickerDialog by remember(date) {
        derivedStateOf {
            (date ?: LocalDate.now()).let {
                DatePickerDialog(
                    context,
                    { _: DatePicker, year: Int, month: Int, day: Int ->
                        LocalDate.of(year, month + 1, day).let { new ->
                            setDate(new)
                            date = new
                        }
                    }, it.year, it.monthValue - 1, it.dayOfMonth
                )
            }
        }
    }
    val isValid by remember(date, validator) {
        derivedStateOf { if (validator == null || date == null) true else validator(date!!)}
    }

    val color by remember(date) {
        derivedStateOf {
            if (date != null)
                if (isValid) Color.Green else Color.Red
            else Color.LightGray
        }
    }

    val textColor by remember(date) {
        derivedStateOf {
            if (date != null)
                Color.Black
            else Color.LightGray
        }
    }

    val colorAnimation by animateColorAsState(targetValue = color)
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
            text = date?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 5.dp)
                .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                    setDate(null)
                    date = null
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
    setPassword: (String) -> Unit,
    validator: ((String) -> Boolean)? = null,
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
            required = true,
            title = title,
            updateValue = setPassword,
            validator = validator,
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
    updateValue: (String) -> Unit,
    validator: ((String) -> Boolean)? = null,
    required: Boolean = false,
    title: String,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var value by rememberSaveable(inputs = arrayOf()) {
        mutableStateOf("")
    }

    val isValid by remember(value, validator) {
        derivedStateOf { validator?.let { it(value)  } }
    }

    val color by remember(isValid, value) {
        derivedStateOf {
            if (value.isNotEmpty() && isValid != null)
                if (isValid!!) Color.Green else Color.Red
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
            value = value,
            onValueChange = {
                value = it
                updateValue(it)
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
