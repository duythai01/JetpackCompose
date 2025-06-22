package com.example.jetpackcomposeapp.data.repository

import com.example.jetpackcomposeapp.data.api.BaseApiService
import com.example.jetpackcomposeapp.data.database.PostDao
import com.example.jetpackcomposeapp.data.database.entities.Post
import com.example.jetpackcomposeapp.data.model.ResultWrapper
import com.example.jetpackcomposeapp.data.model.SimplePost
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository quản lý dữ liệu Post từ cả remote API và local database
 * 
 * Sử dụng pattern:
 * - Offline-first: Hiển thị dữ liệu từ database trước
 * - Network caching: Sync với API và cache vào database
 * - Error handling: Fallback vào local data khi có lỗi network
 */
@Singleton
class PostRepository @Inject constructor(
    private val apiService: BaseApiService,
    private val postDao: PostDao,
    private val gson: Gson
) : BaseRepository<Post, String>() {
    
    /**
     * Lấy tất cả posts với caching strategy
     * 
     * @param forceRefresh Có force refresh từ API hay không
     * @return Flow với ResultWrapper chứa danh sách posts
     */
    fun getAllPosts(forceRefresh: Boolean = false): Flow<ResultWrapper<List<Post>>> {
        return networkBoundResource(forceRefresh)
    }
    
    /**
     * Lấy posts của một user cụ thể
     * 
     * @param userId ID của user
     * @return Flow danh sách posts của user
     */
    fun getPostsByUserId(userId: Int): Flow<List<Post>> {
        return postDao.getPostsByUserId(userId)
    }
    
    /**
     * Tìm kiếm posts theo từ khóa
     * 
     * @param query Từ khóa tìm kiếm
     * @return Flow danh sách posts chứa từ khóa
     */
    fun searchPosts(query: String): Flow<List<Post>> {
        return postDao.searchPosts(query)
    }
    
    /**
     * Thêm post mới vào database
     * 
     * @param post Post cần thêm
     * @return ResultWrapper với ID của post vừa thêm
     */
    suspend fun insertPost(post: Post): ResultWrapper<Long> {
        return safeCall { postDao.insert(post) }
    }
    
    /**
     * Cập nhật post
     * 
     * @param post Post cần cập nhật
     * @return ResultWrapper với số rows được update
     */
    suspend fun updatePost(post: Post): ResultWrapper<Int> {
        return safeCall { postDao.update(post) }
    }
    
    /**
     * Xóa post
     * 
     * @param post Post cần xóa
     * @return ResultWrapper với số rows được xóa
     */
    suspend fun deletePost(post: Post): ResultWrapper<Int> {
        return safeCall { postDao.delete(post) }
    }
    
    /**
     * Xóa tất cả posts
     * 
     * @return ResultWrapper với số rows được xóa
     */
    suspend fun deleteAllPosts(): ResultWrapper<Int> {
        return safeCall { postDao.deleteAllPosts() }
    }
    
    /**
     * Lấy post theo ID
     * 
     * @param postId ID của post
     * @return Flow của Post hoặc null
     */
    fun getPostById(postId: Int): Flow<Post?> {
        return postDao.getPostById(postId)
    }
    
    /**
     * Đếm số posts của user
     * 
     * @param userId ID của user
     * @return ResultWrapper với số lượng posts
     */
    suspend fun getPostCountByUserId(userId: Int): ResultWrapper<Int> {
        return safeCall { postDao.getPostCountByUserId(userId) }
    }
    
    // Override BaseRepository methods
    
    /**
     * Fetch dữ liệu từ JSONPlaceholder API
     */
    override suspend fun fetchFromRemote(): String {
        val response = apiService.get("posts")
        if (response.isSuccessful) {
            return response.body() ?: "[]"
        } else {
            throw Exception("API Error: ${response.code()} ${response.message()}")
        }
    }
    
    /**
     * Parse JSON response thành list Post entities
     */
    override suspend fun parseRemoteData(remoteData: String): List<Post> {
        val type = object : TypeToken<List<SimplePost>>() {}.type
        val simplePosts: List<SimplePost> = gson.fromJson(remoteData, type)
        
        return simplePosts.map { simplePost ->
            Post(
                id = simplePost.id,
                userId = simplePost.userId,
                title = simplePost.title,
                body = simplePost.body,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        }
    }
    
    /**
     * Save posts vào Room database
     */
    override suspend fun saveToLocal(data: List<Post>) {
        postDao.insertAll(data)
    }
    
    /**
     * Fetch posts từ Room database
     */
    override fun fetchFromLocal(): Flow<List<Post>> {
        return postDao.getAllPosts()
    }
} 