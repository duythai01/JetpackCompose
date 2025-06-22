package com.example.jetpackcomposeapp.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.jetpackcomposeapp.data.database.entities.Post
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object cho Post entity
 * 
 * Kế thừa BaseDao để có các phương thức CRUD cơ bản
 * Thêm các query methods đặc biệt cho Post
 */
@Dao
interface PostDao : BaseDao<Post> {
    
    /**
     * Lấy tất cả posts từ database
     * Sử dụng Flow để reactive updates
     * 
     * @return Flow của danh sách posts, tự động update khi database thay đổi
     */
    @Query("SELECT * FROM posts ORDER BY created_at DESC")
    fun getAllPosts(): Flow<List<Post>>
    
    /**
     * Lấy post theo ID
     * 
     * @param postId ID của post cần tìm
     * @return Flow của Post hoặc null nếu không tìm thấy
     */
    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: Int): Flow<Post?>
    
    /**
     * Lấy tất cả posts của một user
     * 
     * @param userId ID của user
     * @return Flow của danh sách posts của user đó
     */
    @Query("SELECT * FROM posts WHERE user_id = :userId ORDER BY created_at DESC")
    fun getPostsByUserId(userId: Int): Flow<List<Post>>
    
    /**
     * Tìm kiếm posts theo title hoặc body
     * 
     * @param searchQuery Từ khóa tìm kiếm
     * @return Flow của danh sách posts chứa từ khóa
     */
    @Query("""
        SELECT * FROM posts 
        WHERE title LIKE '%' || :searchQuery || '%' 
        OR body LIKE '%' || :searchQuery || '%'
        ORDER BY created_at DESC
    """)
    fun searchPosts(searchQuery: String): Flow<List<Post>>
    
    /**
     * Đếm số lượng posts của một user
     * 
     * @param userId ID của user
     * @return Số lượng posts
     */
    @Query("SELECT COUNT(*) FROM posts WHERE user_id = :userId")
    suspend fun getPostCountByUserId(userId: Int): Int
    
    /**
     * Xóa tất cả posts
     * 
     * @return Số lượng rows đã được xóa
     */
    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts(): Int
    
    /**
     * Xóa posts của một user
     * 
     * @param userId ID của user
     * @return Số lượng rows đã được xóa
     */
    @Query("DELETE FROM posts WHERE user_id = :userId")
    suspend fun deletePostsByUserId(userId: Int): Int
    
    /**
     * Lấy posts mới nhất (limit)
     * 
     * @param limit Số lượng posts muốn lấy
     * @return Flow của danh sách posts mới nhất
     */
    @Query("SELECT * FROM posts ORDER BY created_at DESC LIMIT :limit")
    fun getLatestPosts(limit: Int): Flow<List<Post>>
} 