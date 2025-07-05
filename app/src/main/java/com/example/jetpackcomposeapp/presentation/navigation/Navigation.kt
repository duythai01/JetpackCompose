package com.example.jetpackcomposeapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposeapp.presentation.home.HomeContent
import com.example.jetpackcomposeapp.presentation.video_detail.VideoDetailScreen

@Composable
fun Navigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeContent(paddingValues = paddingValues, navController = navController)
        }
        composable(Screen.Shorts.route) {}
        composable(Screen.Create.route) {}
        composable(Screen.Subscriptions.route) {}
        composable(Screen.Library.route) {}
        composable(Screen.VideoDetailScreen.route) {
            VideoDetailScreen(videoUrl = null)
        }
    }
}