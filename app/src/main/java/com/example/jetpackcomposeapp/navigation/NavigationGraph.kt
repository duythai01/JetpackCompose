package com.example.jetpackcomposeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetpackcomposeapp.presentation.example.SimpleExampleScreen

/**
 * Định nghĩa các route trong ứng dụng
 */
object Routes {
    const val EXAMPLE_SCREEN = "example_screen"
    const val HOME_SCREEN = "home_screen"
    // Thêm các route khác ở đây
}

/**
 * Navigation graph chính của ứng dụng
 * 
 * Định nghĩa tất cả các màn hình và cách điều hướng giữa chúng
 * 
 * @param navController Controller để điều khiển navigation
 * @param modifier Modifier cho NavHost
 */
@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.EXAMPLE_SCREEN,
        modifier = modifier
    ) {
        // Màn hình ví dụ minh họa - phiên bản đơn giản
        composable(Routes.EXAMPLE_SCREEN) {
            SimpleExampleScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateTo = { route ->
                    navController.navigate(route)
                }
            )
        }
        
        // Thêm các màn hình khác ở đây
        // composable(Routes.HOME_SCREEN) {
        //     HomeScreen(...)
        // }
    }
} 