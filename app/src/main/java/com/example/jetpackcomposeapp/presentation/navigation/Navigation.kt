package com.example.jetpackcomposeapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposeapp.presentation.home.HomeScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {}
        composable("shorts") {}
        composable("subscriptions") {}
        composable("library") {}
    }
}