package com.example.jetpackcomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.jetpackcomposeapp.presentation.home.HomeScreen
import com.example.jetpackcomposeapp.ui.theme.JetpackcomposeAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity chính của ứng dụng
 * 
 * Được cấu hình với Hilt để hỗ trợ Dependency Injection
 * Thiết lập Navigation và Theme cho toàn bộ ứng dụng
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // hide navigation bar + status bar OS
        /*val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }*/

        setContent {
            JetpackcomposeAppTheme {
               /* val navController = rememberNavController()
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }*/
                HomeScreen()
            }
        }
    }
}
