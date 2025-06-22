package com.example.jetpackcomposeapp.data.model

/**
 * Data class đại diện cho một Post đơn giản
 * 
 * Được sử dụng để minh họa việc quản lý dữ liệu trong ứng dụng
 */
data class SimplePost(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) 