package com.example.jetpackcomposeapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jetpackcomposeapp.data.database.entities.Post

/**
 * Room Database chính của ứng dụng
 * 
 * Cấu hình với:
 * - KSP code generation
 * - Migration support
 * - SQLite backing store
 */
@Database(
    entities = [Post::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Provide PostDao instance
     */
    abstract fun postDao(): PostDao
    
    companion object {
        /**
         * Tên database file
         */
        const val DATABASE_NAME = "app_database"
        
        /**
         * Migration từ version 1 lên version 2 (ví dụ cho tương lai)
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Ví dụ: Thêm column mới
                // database.execSQL("ALTER TABLE posts ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
            }
        }
        
        /**
         * Callback được gọi khi database được tạo lần đầu
         */
        val DATABASE_CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Có thể thêm dữ liệu mẫu vào đây
            }
            
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Các thiết lập khi database được mở
            }
        }
    }
} 