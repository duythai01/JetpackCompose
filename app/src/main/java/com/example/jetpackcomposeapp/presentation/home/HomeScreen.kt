package com.example.jetpackcomposeapp.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcomposeapp.R
import com.example.jetpackcomposeapp.presentation.home.composable.Categories
import com.example.jetpackcomposeapp.presentation.home.composable.ShimmerListVideoPreview
import com.example.jetpackcomposeapp.presentation.home.composable.TopBar
import com.example.jetpackcomposeapp.presentation.home.composable.VideoPreview
import com.example.jetpackcomposeapp.presentation.navigation.Navigation
import com.example.jetpackcomposeapp.presentation.navigation.NavigationBarItems
import com.example.jetpackcomposeapp.ui.theme.JetpackComposeAppTheme
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onCastClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            HomeTopBar(
                onCastClick = onCastClick,
                onNotificationsClick = onNotificationsClick,
                onSearchClick = onSearchClick,
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            HomeBottomNavigation(
                items = NavigationBarItems.items,
                navController = navController,
                onTabSelected = {
                    navController.navigate(it.route)
                }
            )
        }
    ) { paddingValue ->
        Navigation(navController = navController, paddingValues = paddingValue)
    }
}

@Composable
private fun HomeTopBar(
    onCastClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Column {
        TopBar(
            onCastClick = onCastClick,
            onNotificationsClick = onNotificationsClick,
            onSearchClick = onSearchClick,
            onProfileClick = onProfileClick
        )
        Categories()
    }
}

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(true) {
        delay(5000)
        isLoading = false
    }
    LazyColumn(
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 10.dp,
            bottom = paddingValues.calculateBottomPadding()
        ),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(10) { index ->
            ShimmerListVideoPreview(
                isLoading = isLoading,
                contentAfterLoading = {
                    VideoPreview(
                        thumbnailRes = R.drawable.thumbnail,
                        title = "Travel Vlog ${index + 1} - Nha Trang Go Go Go!!!",
                        channelName = "mylinhhhhhhhhhhh",
                        avatarRes = R.drawable.avt,
                        views = "104M",
                        posted = "1 day ago",
                        onClick = {
                            navController.navigate("video_detail/${index + 1}")
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun HomeBottomNavigation(
    items: List<NavigationBarItems>,
    navController: NavController,
    onTabSelected: (NavigationBarItems) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach{ navigationBarItems ->
            NavigationBarItem(
                selected = navigationBarItems.route == currentRoute,
                onClick = { onTabSelected(navigationBarItems) },
                icon = navigationBarItems.icon,
                label = navigationBarItems.title?.let {
                     {
                        Text(
                            text = stringResource(it),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun CreateButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                color = Color.White,
                width = 1.dp,
                shape = CircleShape
            )
            .padding(6.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Create",
            modifier = Modifier.size(20.dp)
        )
    }
}

// Preview functions
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    JetpackComposeAppTheme {
        HomeScreen()
    }
}

