package com.example.jetpackcomposeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetpackcomposeapp.presentation.example.PostDetailScreen
import com.example.jetpackcomposeapp.presentation.example.PostListScreen
import com.example.jetpackcomposeapp.presentation.example.SimpleExampleScreen

/**
 * Định nghĩa các route trong ứng dụng
 */
object Routes {
    const val EXAMPLE_SCREEN = "example_screen"
    const val POST_LIST_SCREEN = "post_list_screen"
    const val POST_DETAIL_SCREEN = "post_detail_screen"
    const val HOME_SCREEN = "home_screen"
    
    // Navigation với parameters
    fun postDetailRoute(postId: Int) = "post_detail_screen/$postId"
    const val POST_DETAIL_ROUTE_WITH_ARGS = "post_detail_screen/{postId}"
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
                onNavigateToPostList = {
                    navController.navigate(Routes.POST_LIST_SCREEN)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateTo = { route ->
                    navController.navigate(route)
                }
            )
        }
        
        // Màn hình danh sách posts với navigation demo
        composable(Routes.POST_LIST_SCREEN) {
            PostListScreen(
                onNavigateToDetail = { postId ->
                    navController.navigate(Routes.postDetailRoute(postId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Màn hình chi tiết post với parameters
        composable(
            route = Routes.POST_DETAIL_ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.IntType
                }
            )
        ) {
            PostDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 