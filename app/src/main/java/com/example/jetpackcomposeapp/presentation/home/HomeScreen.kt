package com.example.jetpackcomposeapp.presentation.home

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposeapp.R

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            Column {
                // top bar
                // categories
            }
        },
        bottomBar = {
            NavigationBar {
                BottomNavigationItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.home),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.shorts),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.shorts),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Box(
                            modifier = Modifier
                                .border(
                                    color = Color.White,
                                    width = 1.dp,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                        ){
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Subscriptions,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        Box(modifier = Modifier.width(60.dp)) {
                            Text(
                                modifier = Modifier.basicMarquee(
                                    iterations = Int.MAX_VALUE,
                                    animationMode = MarqueeAnimationMode.Immediately,
                                    repeatDelayMillis = 500
                                ), // hieu ung chạy chữ khi text dài quá
                                text = stringResource(R.string.subscriptions),
                                style = MaterialTheme.typography.labelMedium,
//                                overflow = TextOverflow.Ellipsis, // nếu text dài quá thì sẽ thêm dấu ...
//                                maxLines = 1,
                            )
                        }

                    }
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.VideoLibrary,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.library),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            // content
        }
    }
}

