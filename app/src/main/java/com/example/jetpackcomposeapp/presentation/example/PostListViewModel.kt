package com.example.jetpackcomposeapp.presentation.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposeapp.data.database.entities.Post
import com.example.jetpackcomposeapp.data.model.ResultWrapper
import com.example.jetpackcomposeapp.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình danh sách posts với navigation demo
 * 
 * Features:
 * - Load posts từ Room database với API sync
 * - Search functionality
 * - Navigation ra Post Detail
 * - Pull-to-refresh
 * - Error handling
 */
@HiltViewModel
class PostListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    
    // StateFlow cho danh sách posts
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    
    // StateFlow cho loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // StateFlow cho error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // StateFlow cho search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // StateFlow cho refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    init {
        // Load posts when ViewModel is created
        loadPosts()
    }
    
    /**
     * Load posts từ repository với offline-first approach
     */
    private fun loadPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts().collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        if (!_isRefreshing.value) {
                            _isLoading.value = true
                        }
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Success -> {
                        _isLoading.value = false
                        _isRefreshing.value = false
                        _posts.value = result.data
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Error -> {
                        _isLoading.value = false
                        _isRefreshing.value = false
                        _errorMessage.value = result.message
                    }
                }
            }
        }
    }
    
    /**
     * Refresh posts từ API
     */
    fun refreshPosts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            postRepository.getAllPosts(forceRefresh = true).collect { result ->
                when (result) {
                    is ResultWrapper.Loading -> {
                        // Keep refreshing state
                    }
                    is ResultWrapper.Success -> {
                        _isRefreshing.value = false
                        _posts.value = result.data
                        _errorMessage.value = null
                    }
                    is ResultWrapper.Error -> {
                        _isRefreshing.value = false
                        _errorMessage.value = result.message
                    }
                }
            }
        }
    }
    
    /**
     * Search posts theo query
     */
    fun searchPosts(query: String) {
        _searchQuery.value = query
        
        if (query.isBlank()) {
            // Nếu query rỗng, load all posts
            loadPosts()
            return
        }
        
        viewModelScope.launch {
            postRepository.searchPosts(query).collect { searchResults ->
                _posts.value = searchResults
            }
        }
    }
    
    /**
     * Clear search và hiển thị tất cả posts
     */
    fun clearSearch() {
        _searchQuery.value = ""
        loadPosts()
    }
    
    /**
     * Thêm post mẫu để test
     */
    fun addSamplePost() {
        viewModelScope.launch {
            val newPost = Post(
                id = System.currentTimeMillis().toInt(),
                userId = 999,
                title = "Post mới từ PostListScreen",
                body = "Đây là post được thêm từ màn hình danh sách để demo chức năng thêm post với Room database.",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            when (val result = postRepository.insertPost(newPost)) {
                is ResultWrapper.Success -> {
                    _errorMessage.value = null
                }
                is ResultWrapper.Error -> {
                    _errorMessage.value = "Lỗi khi thêm post: ${result.message}"
                }
                is ResultWrapper.Loading -> {
                    // Không cần xử lý
                }
            }
        }
    }
    
    /**
     * Xóa một post cụ thể
     */
    fun deletePost(post: Post) {
        viewModelScope.launch {
            when (val result = postRepository.deletePost(post)) {
                is ResultWrapper.Success -> {
                    _errorMessage.value = null
                }
                is ResultWrapper.Error -> {
                    _errorMessage.value = "Lỗi khi xóa post: ${result.message}"
                }
                is ResultWrapper.Loading -> {
                    // Không cần xử lý
                }
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
     * Get post count for display
     */
    fun getPostCount(): Int = _posts.value.size
} 