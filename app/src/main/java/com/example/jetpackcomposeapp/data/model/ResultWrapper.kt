package com.example.jetpackcomposeapp.data.model

/**
 * Lớp wrapper chung để đóng gói kết quả của các cuộc gọi API hoặc các thao tác bất đồng bộ
 * 
 * @param T Kiểu dữ liệu của kết quả trả về
 */
sealed class ResultWrapper<out T> {
    
    /**
     * Trạng thái đang tải dữ liệu
     */
    object Loading : ResultWrapper<Nothing>()
    
    /**
     * Trạng thái thành công với dữ liệu
     * @param data Dữ liệu trả về
     */
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    
    /**
     * Trạng thái lỗi
     * @param message Thông báo lỗi
     * @param exception Exception gốc (tùy chọn)
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : ResultWrapper<Nothing>()
}

/**
 * Extension function để kiểm tra trạng thái thành công
 */
fun <T> ResultWrapper<T>.isSuccess(): Boolean = this is ResultWrapper.Success

/**
 * Extension function để kiểm tra trạng thái loading
 */
fun <T> ResultWrapper<T>.isLoading(): Boolean = this is ResultWrapper.Loading

/**
 * Extension function để kiểm tra trạng thái lỗi
 */
fun <T> ResultWrapper<T>.isError(): Boolean = this is ResultWrapper.Error

/**
 * Extension function để lấy dữ liệu từ Success state
 */
fun <T> ResultWrapper<T>.getDataOrNull(): T? {
    return if (this is ResultWrapper.Success) this.data else null
}

/**
 * Extension function để lấy thông báo lỗi từ Error state
 */
fun <T> ResultWrapper<T>.getErrorMessage(): String? {
    return if (this is ResultWrapper.Error) this.message else null
} 