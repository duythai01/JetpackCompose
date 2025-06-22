package com.example.jetpackcomposeapp.data.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base DAO interface cung cấp các phương thức CRUD cơ bản
 * 
 * Interface này được thiết kế để tái sử dụng cho mọi entity trong ứng dụng
 * Sử dụng Room với KSP để code generation
 * 
 * @param T Generic type của entity
 */
interface BaseDao<T> {
    
    /**
     * Thêm một item vào database
     * 
     * @param item Entity cần thêm
     * @return ID của row vừa được thêm
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T): Long
    
    /**
     * Thêm danh sách items vào database
     * 
     * @param items Danh sách entities cần thêm
     * @return Danh sách IDs của các rows vừa được thêm
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<T>): List<Long>
    
    /**
     * Cập nhật một item trong database
     * 
     * @param item Entity cần cập nhật
     * @return Số lượng rows đã được cập nhật
     */
    @Update
    suspend fun update(item: T): Int
    
    /**
     * Cập nhật danh sách items trong database
     * 
     * @param items Danh sách entities cần cập nhật
     * @return Số lượng rows đã được cập nhật
     */
    @Update
    suspend fun updateAll(items: List<T>): Int
    
    /**
     * Xóa một item khỏi database
     * 
     * @param item Entity cần xóa
     * @return Số lượng rows đã được xóa
     */
    @Delete
    suspend fun delete(item: T): Int
    
    /**
     * Xóa danh sách items khỏi database
     * 
     * @param items Danh sách entities cần xóa
     * @return Số lượng rows đã được xóa
     */
    @Delete
    suspend fun deleteAll(items: List<T>): Int
} 