package com.example.jetpackcomposeapp.presentation.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposeapp.R
import com.example.jetpackcomposeapp.presentation.home.CreateButton

sealed class Screen(
    val title: Int? = null,
    val icon: @Composable () -> Unit,
    val route: String
) {
    data object Home : Screen(
        title = R.string.home,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = stringResource(R.string.home),
                modifier = Modifier.size(24.dp)
            )
        },
        route = "home"
    )

    data object Shorts : Screen(
        title = R.string.shorts,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.shorts),
                contentDescription = stringResource(R.string.shorts),
                modifier = Modifier.size(24.dp)
            )
        },
        route = "shorts"
    )

    data object Subscriptions : Screen(
        title = R.string.subscriptions,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Subscriptions,
                contentDescription = stringResource(R.string.subscriptions),
                modifier = Modifier.size(24.dp)
            )
        },
        route = "subscriptions"
    )

    data object Library : Screen(
        title = R.string.library,
        icon = {
            Icon(
                imageVector = Icons.Outlined.VideoLibrary,
                contentDescription = stringResource(R.string.library),
                modifier = Modifier.size(24.dp)
            )
        },
        route = "library"
    )

    data object Create : Screen(
        title = null,
        icon = {
            CreateButton()
        },
        route = "create"
    )

    data object VideoDetailScreen : Screen(
        title = null,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.shorts),
                contentDescription = stringResource(R.string.shorts),
                modifier = Modifier.size(24.dp)
            )
        },
        route = "video_detail_screen"
    )

    companion object{
        val items = listOf(Home, Shorts, Create, Subscriptions, Library)
    }
}