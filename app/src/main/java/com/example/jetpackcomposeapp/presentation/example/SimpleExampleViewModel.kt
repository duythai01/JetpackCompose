package com.example.jetpackcomposeapp.presentation.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposeapp.data.api.BaseApiService
import com.example.jetpackcomposeapp.data.model.SimplePost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình ví dụ sử dụng Hilt Dependency Injection
 * 
 * Được cấu hình với Hilt để inject các dependencies:
 * - BaseApiService cho API calls
 * 
 * Minh họa các thành phần cơ bản:
 * - State management với StateFlow
 * - API calls với Retrofit
 * - Mock WebSocket functionality
 * - Loading states
 */
@HiltViewModel
class SimpleExampleViewModel @Inject constructor(
    private val apiService: BaseApiService
) : ViewModel() {
    
    // StateFlow cho danh sách posts
    private val _posts = MutableStateFlow<List<SimplePost>>(emptyList())
    val posts: StateFlow<List<SimplePost>> = _posts.asStateFlow()
    
    // StateFlow cho loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // StateFlow cho tin nhắn WebSocket cuối cùng
    private val _lastWebSocketMessage = MutableStateFlow("")
    val lastWebSocketMessage: StateFlow<String> = _lastWebSocketMessage.asStateFlow()
    
    // StateFlow cho trạng thái kết nối WebSocket
    private val _webSocketConnectionState = MutableStateFlow("Chưa kết nối")
    val webSocketConnectionState: StateFlow<String> = _webSocketConnectionState.asStateFlow()
    
    // Mock data
    private val mockPosts = listOf(
        SimplePost(1, 1, "Bài viết đầu tiên", "Đây là nội dung của bài viết đầu tiên từ API giả lập."),
        SimplePost(2, 1, "Bài viết thứ hai", "Nội dung của bài viết thứ hai với nhiều thông tin hơn."),
        SimplePost(3, 2, "Bài viết từ user khác", "Đây là bài viết từ một user khác trong hệ thống."),
        SimplePost(4, 2, "Jetpack Compose", "Hướng dẫn sử dụng Jetpack Compose cho Android development."),
        SimplePost(5, 3, "MVVM Architecture", "Kiến trúc MVVM trong Android với ViewModel và LiveData/StateFlow.")
    )
    
    init {
        // Tự động load posts khi ViewModel được khởi tạo
        loadPosts()
    }
    
    /**
     * Tải danh sách posts (giả lập API call)
     */
    fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            // Giả lập network delay
            delay(1500)
            _posts.value = mockPosts
            _isLoading.value = false
        }
    }
    
    /**
     * Thêm một post mẫu vào danh sách
     */
    fun addSamplePost() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500) // Giả lập processing time
            
            val newPost = SimplePost(
                id = _posts.value.size + 100,
                userId = 99,
                title = "Post được thêm từ app",
                body = "Đây là một post mẫu được thêm trực tiếp từ ứng dụng để minh họa chức năng thêm dữ liệu."
            )
            
            _posts.value = _posts.value + newPost
            _isLoading.value = false
        }
    }
    
    /**
     * Xóa tất cả posts
     */
    fun clearAllPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500) // Giả lập processing time
            _posts.value = emptyList()
            _isLoading.value = false
        }
    }
    
    /**
     * Kết nối WebSocket (giả lập)
     */
    fun connectWebSocket() {
        viewModelScope.launch {
            _webSocketConnectionState.value = "Đang kết nối..."
            delay(1000) // Giả lập connection time
            _webSocketConnectionState.value = "Đã kết nối"
        }
    }
    
    /**
     * Ngắt kết nối WebSocket (giả lập)
     */
    fun disconnectWebSocket() {
        _webSocketConnectionState.value = "Chưa kết nối"
        _lastWebSocketMessage.value = ""
    }
    
    /**
     * Gửi tin nhắn WebSocket mẫu (giả lập)
     */
    fun sendSampleWebSocketMessage() {
        if (_webSocketConnectionState.value == "Đã kết nối") {
            viewModelScope.launch {
                val message = "Tin nhắn từ Android App - ${System.currentTimeMillis()}"
                
                // Giả lập gửi tin nhắn và nhận echo response
                delay(200)
                _lastWebSocketMessage.value = "Echo: $message"
            }
        }
    }
    
    /**
     * Reset tất cả states về trạng thái ban đầu
     */
    fun resetAll() {
        _posts.value = emptyList()
        _isLoading.value = false
        _lastWebSocketMessage.value = ""
        _webSocketConnectionState.value = "Chưa kết nối"
    }
} 