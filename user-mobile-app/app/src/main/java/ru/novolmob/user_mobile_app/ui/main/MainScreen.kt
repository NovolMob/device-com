@file:OptIn(ExperimentalAnimationApi::class)

package ru.novolmob.user_mobile_app.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import ru.novolmob.user_mobile_app.models.NavigationTab
import ru.novolmob.user_mobile_app.navigation.MainNavigation
import ru.novolmob.user_mobile_app.navigation.NavigationRoute


@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        tabs = NavigationRoute.Main.tabs.mapNotNull { it.navigationTab }
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    tabs: List<NavigationTab>,
    navHostController: NavHostController = rememberAnimatedNavController(),
    innerNavHostController: NavHostController = rememberAnimatedNavController()
) {
    val destination by innerNavHostController.currentBackStackEntryAsState()
    val selected by remember(tabs, destination) {
        derivedStateOf { tabs.find {
            destination?.destination?.route?.startsWith(it.route) == true }
        }
    }

    Box(
        modifier = modifier
            .background(color = Color.White)
            .fillMaxSize(),
    ) {
        MainNavigation(
            modifier = Modifier,
            navHostController = innerNavHostController,
            startDestination = NavigationRoute.Main.Catalog,
            tabs = tabs
        )
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(0.9f)
                .padding(bottom = 5.dp)
                .height(60.dp),
            navHostController = innerNavHostController,
            tabs = tabs,
            selected = selected
        )
    }
}

@Composable
private fun NavigationBar(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    selected: NavigationTab?,
    tabs: List<NavigationTab>
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(25))
            .background(color = Color.LightGray.copy(alpha = 0.8f), shape = RoundedCornerShape(25)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach {
            val isSelected by remember(selected, it) {
                derivedStateOf { it == selected }
            }
            NavigationBarItem(
                modifier = Modifier,
                navHostController = navHostController,
                selected = isSelected,
                navigationTab = it
            )
        }
    }
}

@Composable
private fun NavigationBarItem(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    selected: Boolean,
    navigationTab: NavigationTab
) {
    val badge by navigationTab.badgeFlow.collectAsState(initial = 0)
    val hasBadge by remember(badge) {
        derivedStateOf { badge > 0 }
    }
    val badgeString by remember {
        derivedStateOf { if (badge > 99) "..." else badge.toString() }
    }
    val badgeSizeAnimation by animateValueAsState(
        targetValue = if (hasBadge) 14.sp else 0.sp,
        typeConverter = TwoWayConverter({ AnimationVector1D(it.value) }, { it.value.sp })
    )
    
    val iconColor by remember(selected) {
        derivedStateOf { if (selected) Color.Black else Color.Gray }
    }
    val iconSize by animateDpAsState(targetValue = if (selected) 40.dp else 30.dp)
    Column(
        modifier = modifier
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                navigationTab.navigate(navHostController)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(iconSize),
                imageVector = navigationTab.imageVector(),
                contentDescription = null,
                tint = iconColor
            )
            if (hasBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(color = Color.Red, shape = CircleShape)
                        .padding(horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = badgeString,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = badgeSizeAnimation,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Text(
            modifier = Modifier,
            text = stringResource(id = navigationTab.displayName),
            color = Color.Black,
            maxLines = 1,
            fontSize = 12.sp
        )
    }
}