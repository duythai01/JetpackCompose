package com.example.jetpackcomposeapp.di

import android.content.Context
import androidx.room.Room
import com.example.jetpackcomposeapp.data.database.AppDatabase
import com.example.jetpackcomposeapp.data.database.PostDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module cho các dependencies liên quan đến Database
 * 
 * Cung cấp:
 * - Room Database instance
 * - DAOs
 * - Database configuration
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Cung cấp Room Database instance
     * 
     * Sử dụng Singleton scope để đảm bảo chỉ có một instance database
     * trong toàn bộ vòng đời của ứng dụng
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            // Cấu hình migrations (nếu có)
            .addMigrations(AppDatabase.MIGRATION_1_2)
            
            // Database callback cho các event
            .addCallback(AppDatabase.DATABASE_CALLBACK)
            
            // Fallback strategy khi không có migration
            // Chỉ sử dụng trong development, production nên có migration plan
            .fallbackToDestructiveMigration()
            
            // Cho phép main thread queries (chỉ trong trường hợp đặc biệt)
            // .allowMainThreadQueries() // Không khuyến khích
            
            .build()
    }

    /**
     * Cung cấp PostDao từ database
     */
    @Provides
    fun providePostDao(database: AppDatabase): PostDao {
        return database.postDao()
    }

    /**
     * Cung cấp Gson instance cho JSON parsing
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
} 