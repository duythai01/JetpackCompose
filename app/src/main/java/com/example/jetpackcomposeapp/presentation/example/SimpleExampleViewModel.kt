package com.example.jetpackcomposeapp.presentation.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposeapp.data.api.BaseApiService
import com.example.jetpackcomposeapp.data.database.entities.Post
import com.example.jetpackcomposeapp.data.model.ResultWrapper
import com.example.jetpackcomposeapp.data.repository.PostRepository
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
 * - PostRepository cho quản lý data từ API và Room Database
 * - BaseApiService cho các API calls khác
 * 
 * Minh họa các thành phần cơ bản:
 * - State management với StateFlow
 * - Room Database operations với caching
 * - API calls với Retrofit
 * - Mock WebSocket functionality
 * - Loading states và error handling
 */
@HiltViewModel
class SimpleExampleViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val apiService: BaseApiService
) : ViewModel() {
    
    // StateFlow cho danh sách posts từ Room database
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    
    // StateFlow cho loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // StateFlow cho error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // StateFlow cho tin nhắn WebSocket cuối cùng
    private val _lastWebSocketMessage = MutableStateFlow("")
    val lastWebSocketMessage: StateFlow<String> = _lastWebSocketMessage.asStateFlow()
    
    // StateFlow cho trạng thái kết nối WebSocket
    private val _webSocketConnectionState = MutableStateFlow("Chưa kết nối")
    val webSocketConnectionState: StateFlow<String> = _webSocketConnectionState.asStateFlow()
    
    init {
        // Tự động load posts khi ViewModel được khởi tạo
        loadPosts()
        
        // Observe posts từ repository với caching
        observePosts()
    }
    
    /**
     * Observe posts từ PostRepository với Room database caching
     */
    private fun observePosts() {
        viewModelScope.launch {
            postRepository.getAllPosts().collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        _isLoading.value = true
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Success -> {
                        _isLoading.value = false
                        _posts.value = result.data
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = result.message
                    }
                }
            }
        }
    }
    
    /**
     * Tải danh sách posts từ API với force refresh
     */
    fun loadPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts(forceRefresh = true).collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        _isLoading.value = true
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Success -> {
                        _isLoading.value = false
                        _posts.value = result.data
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Error -> {
                        _isLoading.value = false
                        _errorMessage.value = result.message
                    }
                }
            }
        }
    }
    
    /**
     * Thêm một post mẫu vào database
     */
    fun addSamplePost() {
        viewModelScope.launch {
            _isLoading.value = true
            
            val newPost = Post(
                id = System.currentTimeMillis().toInt(), // Tạm thời sử dụng timestamp làm ID
                userId = 99,
                title = "Post được thêm từ app",
                body = "Đây là một post mẫu được thêm trực tiếp từ ứng dụng để minh họa chức năng thêm dữ liệu vào Room database.",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            when (val result = postRepository.insertPost(newPost)) {
                is ResultWrapper.Success -> {
                    _errorMessage.value = null
                    // Posts sẽ được update tự động qua observe
                }
                is ResultWrapper.Error -> {
                    _errorMessage.value = "Lỗi khi thêm post: ${result.message}"
                }
                is ResultWrapper.Loading -> {
                    // Không cần xử lý
                }
            }
            
            _isLoading.value = false
        }
    }
    
    /**
     * Xóa tất cả posts khỏi database
     */
    fun clearAllPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            
            when (val result = postRepository.deleteAllPosts()) {
                is ResultWrapper.Success -> {
                    _errorMessage.value = null
                    // Posts sẽ được update tự động qua observe
                }
                is ResultWrapper.Error -> {
                    _errorMessage.value = "Lỗi khi xóa posts: ${result.message}"
                }
                is ResultWrapper.Loading -> {
                    // Không cần xử lý
                }
            }
            
            _isLoading.value = false
        }
    }
    
    /**
     * Tìm kiếm posts theo từ khóa
     */
    fun searchPosts(query: String) {
        if (query.isBlank()) {
            // Nếu query rỗng, hiển thị tất cả posts
            observePosts()
            return
        }
        
        viewModelScope.launch {
            postRepository.searchPosts(query).collect { searchResults ->
                _posts.value = searchResults
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
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