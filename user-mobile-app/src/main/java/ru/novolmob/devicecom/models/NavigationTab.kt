package ru.novolmob.devicecom.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow

data class NavigationTab(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    @StringRes val displayName: Int,
    private val imageVector: ImageVector? = null,
    @DrawableRes private val imageVectorId: Int? = null,
    val badgeFlow: Flow<Int> = snapshotFlow { 0 },
    val navigate: NavHostController.() -> Unit = {},
    val content: @Composable (AnimatedVisibilityScope.(Modifier, NavHostController, NavBackStackEntry) -> Unit),
) {
    override fun equals(other: Any?): Boolean {
        if (other !is NavigationTab) return false
        if (other.hashCode() != hashCode()) return false
        return true
    }

    override fun hashCode(): Int {
        return route.hashCode()
    }

    @Composable
    fun imageVector(): ImageVector =
        imageVector
            ?: imageVectorId?.let { ImageVector.vectorResource(id = it) }
            ?: Icons.Default.FavoriteBorder
}