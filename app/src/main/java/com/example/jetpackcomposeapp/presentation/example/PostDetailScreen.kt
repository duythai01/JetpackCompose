package com.example.jetpackcomposeapp.presentation.example

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Check
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
 * Màn hình chi tiết post với edit functionality
 * 
 * Features:
 * - Hiển thị chi tiết post từ Room database
 * - Edit mode để chỉnh sửa title/body
 * - Delete post với confirmation
 * - Navigation back với unsaved changes warning
 * - Error handling
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: PostDetailViewModel = hiltViewModel()
) {
    // Collect states
    val post by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val editTitle by viewModel.editTitle.collectAsState()
    val editBody by viewModel.editBody.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    
    // Local state for dialogs
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUnsavedChangesDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    text = if (isEditing) "Chỉnh sửa Post" else "Chi tiết Post",
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    if (isEditing && viewModel.hasUnsavedChanges()) {
                        showUnsavedChangesDialog = true
                    } else {
                        onNavigateBack()
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                if (post != null) {
                    // Refresh button
                    IconButton(
                        onClick = { viewModel.refreshPost() },
                        enabled = !isLoading && !isSaving
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    
                    if (isEditing) {
                        // Save button
                        IconButton(
                            onClick = { viewModel.savePost() },
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.Check, contentDescription = "Save")
                            }
                        }
                    } else {
                        // Edit button
                        IconButton(
                            onClick = { viewModel.toggleEditMode() },
                            enabled = !isLoading
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                    
                    // Delete button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        enabled = !isLoading && !isSaving
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        )
        
        // Content
        if (isLoading && post == null) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Đang tải post...")
                }
            }
        } else if (post != null) {
            // Post content
            PostDetailContent(
                post = post!!,
                isEditing = isEditing,
                editTitle = editTitle,
                editBody = editBody,
                onTitleChange = viewModel::updateEditTitle,
                onBodyChange = viewModel::updateEditBody,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Empty/Error state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Không tìm thấy post",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Post có thể đã bị xóa",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Button(onClick = onNavigateBack) {
                        Text("Quay lại")
                    }
                }
            }
        }
    }
    
    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa post này? Hành động này không thể hoàn tác.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deletePost { onNavigateBack() }
                    }
                ) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
    
    // Unsaved Changes Dialog
    if (showUnsavedChangesDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedChangesDialog = false },
            title = { Text("Có thay đổi chưa lưu") },
            text = { Text("Bạn có muốn rời khỏi mà không lưu thay đổi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUnsavedChangesDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("Rời khỏi", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnsavedChangesDialog = false }) {
                    Text("Ở lại")
                }
            }
        )
    }
    
    // Error Dialog
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
}

/**
 * Content hiển thị chi tiết post
 */
@Composable
private fun PostDetailContent(
    post: Post,
    isEditing: Boolean,
    editTitle: String,
    editBody: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Post Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Thông tin Post",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "ID: ${post.id}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = "User ID: ${post.userId}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = "Created: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(post.createdAt))}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                if (post.updatedAt != post.createdAt) {
                    Text(
                        text = "Updated: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(post.updatedAt))}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
        
        // Title Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tiêu đề",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (isEditing) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Nhập tiêu đề...") },
                        singleLine = false,
                        maxLines = 3
                    )
                } else {
                    Text(
                        text = post.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        // Body Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Nội dung",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (isEditing) {
                    OutlinedTextField(
                        value = editBody,
                        onValueChange = onBodyChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        placeholder = { Text("Nhập nội dung...") },
                        singleLine = false
                    )
                } else {
                    Text(
                        text = post.body,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Edit Mode Instructions
        if (isEditing) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "💡 Nhấn nút Save để lưu thay đổi hoặc nút Back để hủy",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
} 