package com.example.jetpackcomposeapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Lớp Application chính của ứng dụng
 * 
 * Được cấu hình với Hilt để quản lý Dependency Injection
 */
@HiltAndroidApp
class BaseApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Khởi tạo các thành phần global nếu cần thiết
    }
} 