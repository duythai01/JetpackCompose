package com.example.jetpackcomposeapp.presentation.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposeapp.R
import com.example.jetpackcomposeapp.ui.theme.JetpackComposeAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onCastClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            AppLogo()
        },
        actions = {
            TopBarActions(
                onCastClick = onCastClick,
                onNotificationsClick = onNotificationsClick,
                onSearchClick = onSearchClick,
                onProfileClick = onProfileClick
            )
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors()
    )
}

@Composable
private fun TopBarActions(
    onCastClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        TopBarIconButton(
            icon = Icons.Rounded.Cast,
            contentDescription = "Cast",
            onClick = onCastClick
        )
        
        TopBarIconButton(
            icon = Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            onClick = onNotificationsClick
        )
        
        TopBarIconButton(
            icon = Icons.Outlined.Search,
            contentDescription = "Search",
            onClick = onSearchClick
        )
        
        TopBarIconButton(
            icon = Icons.Outlined.Person,
            contentDescription = "Profile",
            onClick = onProfileClick
        )
    }
}

@Composable
private fun TopBarIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "YouTube Logo",
        modifier = modifier
            .fillMaxWidth(0.4f)
            .height(28.dp)
            .width(90.dp)
    )
}

// Preview functions
@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    JetpackComposeAppTheme {
        TopBar()
    }
}

@Preview(showBackground = true)
@Composable
private fun AppLogoPreview() {
    JetpackComposeAppTheme {
        AppLogo()
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarActionsPreview() {
    JetpackComposeAppTheme {
        TopBarActions(
            onCastClick = {},
            onNotificationsClick = {},
            onSearchClick = {},
            onProfileClick = {}
        )
    }
}