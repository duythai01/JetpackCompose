package com.example.jetpackcomposeapp.presentation.example

import androidx.lifecycle.SavedStateHandle
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
 * ViewModel cho màn hình chi tiết post
 * 
 * Features:
 * - Load post từ Room database theo ID
 * - Update post title/body
 * - Delete post
 * - Error handling
 */
@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // Get post ID từ navigation arguments
    private val postId: Int = checkNotNull(savedStateHandle["postId"])
    
    // StateFlow cho post detail
    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post.asStateFlow()
    
    // StateFlow cho loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // StateFlow cho error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // StateFlow cho edit mode
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()
    
    // StateFlow cho edit fields
    private val _editTitle = MutableStateFlow("")
    val editTitle: StateFlow<String> = _editTitle.asStateFlow()
    
    private val _editBody = MutableStateFlow("")
    val editBody: StateFlow<String> = _editBody.asStateFlow()
    
    // StateFlow cho save loading
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
    
    init {
        loadPost()
    }
    
    /**
     * Load post chi tiết từ Room database
     */
    private fun loadPost() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            postRepository.getPostById(postId).collect { post ->
                _isLoading.value = false
                _post.value = post
                
                if (post == null) {
                    _errorMessage.value = "Không tìm thấy post với ID: $postId"
                } else {
                    // Initialize edit fields
                    _editTitle.value = post.title
                    _editBody.value = post.body
                }
            }
        }
    }
    
    /**
     * Toggle edit mode
     */
    fun toggleEditMode() {
        _isEditing.value = !_isEditing.value
        
        if (!_isEditing.value) {
            // Reset edit fields when canceling edit
            _post.value?.let { post ->
                _editTitle.value = post.title
                _editBody.value = post.body
            }
        }
    }
    
    /**
     * Update edit title
     */
    fun updateEditTitle(title: String) {
        _editTitle.value = title
    }
    
    /**
     * Update edit body
     */
    fun updateEditBody(body: String) {
        _editBody.value = body
    }
    
    /**
     * Save changes to post
     */
    fun savePost() {
        val currentPost = _post.value ?: return
        
        if (_editTitle.value.isBlank()) {
            _errorMessage.value = "Title không được để trống"
            return
        }
        
        if (_editBody.value.isBlank()) {
            _errorMessage.value = "Body không được để trống"
            return
        }
        
        viewModelScope.launch {
            _isSaving.value = true
            
            val updatedPost = currentPost.copy(
                title = _editTitle.value,
                body = _editBody.value,
                updatedAt = System.currentTimeMillis()
            )
            
            when (val result = postRepository.updatePost(updatedPost)) {
                is ResultWrapper.Success -> {
                    _isSaving.value = false
                    _isEditing.value = false
                    _errorMessage.value = null
                    // Post sẽ được update tự động thông qua Flow
                }
                is ResultWrapper.Error -> {
                    _isSaving.value = false
                    _errorMessage.value = "Lỗi khi cập nhật post: ${result.message}"
                }
                is ResultWrapper.Loading -> {
                    // Không cần xử lý
                }
            }
        }
    }
    
    /**
     * Delete current post
     */
    fun deletePost(onPostDeleted: () -> Unit) {
        val currentPost = _post.value ?: return
        
        viewModelScope.launch {
            _isLoading.value = true
            
            when (val result = postRepository.deletePost(currentPost)) {
                is ResultWrapper.Success -> {
                    _isLoading.value = false
                    _errorMessage.value = null
                    onPostDeleted() // Navigate back
                }
                is ResultWrapper.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Lỗi khi xóa post: ${result.message}"
                }
                is ResultWrapper.Loading -> {
                    // Không cần xử lý
                }
            }
        }
    }
    
    /**
     * Refresh post from database
     */
    fun refreshPost() {
        loadPost()
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * Check if post has been modified
     */
    fun hasUnsavedChanges(): Boolean {
        val currentPost = _post.value ?: return false
        return _editTitle.value != currentPost.title || 
               _editBody.value != currentPost.body
    }
} 