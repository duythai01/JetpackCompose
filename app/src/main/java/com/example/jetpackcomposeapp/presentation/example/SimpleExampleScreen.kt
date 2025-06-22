package com.example.jetpackcomposeapp.presentation.example

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpackcomposeapp.data.database.entities.Post

/**
 * Màn hình ví dụ minh họa các thành phần base cơ bản với Hilt DI
 * 
 * Sử dụng Hilt để inject ViewModel và các dependencies
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleExampleScreen(
    onNavigateToPostList: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateTo: (String) -> Unit = {},
    viewModel: SimpleExampleViewModel = hiltViewModel()
) {
    // Collect state từ ViewModel
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val lastWebSocketMessage by viewModel.lastWebSocketMessage.collectAsState()
    val webSocketConnectionState by viewModel.webSocketConnectionState.collectAsState()
    
    // State cho alert dialog
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Base Platform Demo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section WebSocket
                WebSocketSection(
                    connectionState = webSocketConnectionState,
                    lastMessage = lastWebSocketMessage,
                    onConnect = { viewModel.connectWebSocket() },
                    onDisconnect = { viewModel.disconnectWebSocket() },
                    onSendMessage = { viewModel.sendSampleWebSocketMessage() }
                )
                
                NavigationSection(
                    onNavigateToPostList = onNavigateToPostList
                )
                
                HorizontalDivider()
                
                // Section thao tác với posts
                PostActionsSection(
                    onRefresh = { 
                        viewModel.loadPosts()
                        alertMessage = "Đang tải lại dữ liệu từ API..."
                        showAlert = true
                    },
                    onAddPost = { 
                        viewModel.addSamplePost()
                        alertMessage = "Đang thêm post vào database..."
                        showAlert = true
                    },
                    onClearAll = { 
                        viewModel.clearAllPosts()
                        alertMessage = "Đang xóa tất cả posts..."
                        showAlert = true
                    }
                )
                
                HorizontalDivider()
                
                // Danh sách posts
                PostListSection(
                    posts = posts,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Đang tải dữ liệu...",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Error Alert Dialog
    errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Lỗi") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }
    
    // Alert Dialog
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Thông báo") },
            text = { Text(alertMessage) },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text("OK")
                }
            }
        )
    }
}

/**
 * Section hiển thị thông tin và điều khiển WebSocket
 */
@Composable
private fun WebSocketSection(
    connectionState: String,
    lastMessage: String,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onSendMessage: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "WebSocket Demo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Trạng thái: $connectionState",
                fontSize = 14.sp,
                color = if (connectionState == "Đã kết nối") Color.Green else Color.Gray
            )
            
            if (lastMessage.isNotEmpty()) {
                Text(
                    text = "Tin nhắn cuối: $lastMessage",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onConnect,
                    enabled = connectionState != "Đã kết nối"
                ) {
                    Text("Kết nối")
                }
                
                Button(
                    onClick = onDisconnect,
                    enabled = connectionState == "Đã kết nối"
                ) {
                    Text("Ngắt kết nối")
                }
                
                Button(
                    onClick = onSendMessage,
                    enabled = connectionState == "Đã kết nối"
                ) {
                    Text("Gửi tin nhắn")
                }
            }
        }
    }
}

/**
 * Section các thao tác với posts
 */
@Composable
private fun PostActionsSection(
    onRefresh: () -> Unit,
    onAddPost: () -> Unit,
    onClearAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Quản lý Posts",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onRefresh) {
                    Text("Tải lại")
                }
                
                Button(onClick = onAddPost) {
                    Text("Thêm Post")
                }
                
                OutlinedButton(onClick = onClearAll) {
                    Text("Xóa tất cả")
                }
            }
        }
    }
}

/**
 * Section hiển thị danh sách posts
 */
@Composable
private fun PostListSection(
    posts: List<Post>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Danh sách Posts (${posts.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (posts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chưa có dữ liệu",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(posts) { post ->
                        PostItem(post = post)
                    }
                }
            }
        }
    }
}

/**
 * Item hiển thị thông tin một post từ Room database
 */
@Composable
private fun PostItem(post: Post) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "ID: ${post.id} | User: ${post.userId}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            
            Text(
                text = post.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            
            Text(
                text = post.body,
                fontSize = 12.sp,
                color = Color.DarkGray,
                maxLines = 3
            )
            
            Text(
                text = "Created: ${java.text.SimpleDateFormat("HH:mm dd/MM").format(java.util.Date(post.createdAt))}",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Section cho navigation demo
 */
@Composable
private fun NavigationSection(
    onNavigateToPostList: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "🧭 Navigation Demo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Navigation Patterns Demo:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "• Push Navigation - Chuyển đến màn hình mới\n" +
                          "• Back Navigation - Quay lại màn hình trước\n" + 
                          "• Parameter Navigation - Truyền dữ liệu\n" +
                          "• Deep Linking - Navigation với arguments",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
        }
        
        Button(
            onClick = onNavigateToPostList,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🚀 Xem Post List (Room + Navigation)")
        }
    }
}