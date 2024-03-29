@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)

package ru.novolmob.devicecom

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import ru.novolmob.devicecom.models.ProfileAction
import ru.novolmob.devicecom.utils.ScreenNotification
import ru.novolmob.devicecom.navigation.GeneralNavigation
import ru.novolmob.devicecom.navigation.NavigationRoute.Authorization.navigateToAuthorization
import ru.novolmob.devicecom.navigation.NavigationRoute.Main.Companion.navigateToMain
import ru.novolmob.devicecom.services.IProfileService
import ru.novolmob.devicecom.ui.theme.UsermobileappTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val profileService: IProfileService by inject()
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.isSystemBarsVisible = true
                systemUiController.setStatusBarColor(Color.White)
                systemUiController.setNavigationBarColor(Color.White)
            }

            UsermobileappTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberAnimatedNavController()

                    LaunchedEffect(key1 = true) {
                        profileService.action.collectLatest {
                            when (it) {
                                is ProfileAction.Login -> { navHostController.navigateToMain() }
                                is ProfileAction.Logout -> { navHostController.navigateToAuthorization() }
                                is ProfileAction.Registered -> { navHostController.navigateToAuthorization() }
                                else -> {}
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        GeneralNavigation(
                            modifier = Modifier
                                .systemBarsPadding(),
                            navHostController = navHostController
                        )
                        Notification(
                            modifier = Modifier
                                .systemBarsPadding()
                                .align(Alignment.TopCenter)
                                .padding(30.dp),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun Notification(
        modifier: Modifier = Modifier,
    ) {
        val notification by ScreenNotification.notification.collectAsState()

        val alphaAnimation by animateFloatAsState(
            targetValue = if (notification?.visible == true) 1f else 0f,
            animationSpec = tween(durationMillis = 400)
        )

        Text(
            modifier = modifier
                .graphicsLayer {
                    alpha = alphaAnimation
                }
                .heightIn(max = 300.dp)
                .border(
                    width = 1.dp,
                    color = notification?.backgroundColor ?: Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = notification?.backgroundColor?.copy(alpha = 0.1f) ?: Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(15.dp),
            text = notification?.message ?: "",
            color = notification?.textColor ?: Color.Transparent,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ExperimentalBasicText() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }
    val hasUser by remember(user) { derivedStateOf { user.isNotEmpty() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue)
    ) {
        if (hasUser) {
            Text(text = "Hello $user!")
        }


        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color.Red),
            value = email,
            onValueChange = { email = it },
            singleLine = true
        )

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color.Cyan),
            value = password,
            onValueChange = { password = it },
            keyboardActions = KeyboardActions(onDone = {
                user = email
            }),
            singleLine = true,
        )
    }
}