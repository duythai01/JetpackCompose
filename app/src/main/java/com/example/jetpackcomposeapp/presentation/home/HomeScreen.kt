package com.example.jetpackcomposeapp.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.jetpackcomposeapp.R
import com.example.jetpackcomposeapp.presentation.home.composable.TopBar
import com.example.jetpackcomposeapp.ui.theme.JetpackComposeAppTheme

@Composable
fun HomeScreen(
    onNavigateToShorts: () -> Unit = {},
    onNavigateToCreate: () -> Unit = {},
    onNavigateToSubscriptions: () -> Unit = {},
    onNavigateToLibrary: () -> Unit = {},
    onCastClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

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
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index ->
                    selectedTabIndex = index
                    when (index) {
                        0 -> { /* Home - already here */ }
                        1 -> onNavigateToShorts()
                        2 -> onNavigateToCreate()
                        3 -> onNavigateToSubscriptions()
                        4 -> onNavigateToLibrary()
                    }
                }
            )
        }
    ) { paddingValues ->
        HomeContent(
            modifier = Modifier.padding(paddingValues)
        )
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
        // TODO: Implement categories horizontal scroll
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // TODO: Implement video list
        // TODO: Implement lazy loading
    }
}

@Composable
private fun HomeBottomNavigation(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        // Home Tab
        NavigationBarItem(
            selected = selectedTabIndex == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = stringResource(R.string.home),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.home),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                )
            }
        )

        // Shorts Tab
        NavigationBarItem(
            selected = selectedTabIndex == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.shorts),
                    contentDescription = stringResource(R.string.shorts),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.shorts),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                )
            }
        )

        // Create Tab (no label, special styling)
        NavigationBarItem(
            selected = selectedTabIndex == 2,
            onClick = { onTabSelected(2) },
            icon = {
                CreateButton()
            },
            label = null
        )

        // Subscriptions Tab
        NavigationBarItem(
            selected = selectedTabIndex == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Subscriptions,
                    contentDescription = stringResource(R.string.subscriptions),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.subscriptions),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                )
            }
        )

        // Library Tab
        NavigationBarItem(
            selected = selectedTabIndex == 4,
            onClick = { onTabSelected(4) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.VideoLibrary,
                    contentDescription = stringResource(R.string.library),
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.library),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                )
            }
        )
    }
}

@Composable
private fun CreateButton(
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

@Preview(showBackground = true)
@Composable
private fun HomeBottomNavigationPreview() {
    JetpackComposeAppTheme{
        HomeBottomNavigation(
            selectedTabIndex = 0,
            onTabSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateButtonPreview() {
    JetpackComposeAppTheme {
        CreateButton()
    }
}

