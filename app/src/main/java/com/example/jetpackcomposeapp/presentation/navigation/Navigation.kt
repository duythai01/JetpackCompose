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
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeContent(paddingValues = paddingValues, navController = navController)
        }
        composable("shorts") {}
        composable("subscriptions") {}
        composable("library") {}
        composable("video_detail/{video_url}") { backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("video_url")
            VideoDetailScreen(videoUrl = videoUrl)
        }
    }
}