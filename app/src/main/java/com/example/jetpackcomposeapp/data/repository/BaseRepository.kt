package com.example.jetpackcomposeapp.data.repository

import com.example.jetpackcomposeapp.data.model.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Abstract BaseRepository cung cấp pattern cơ bản cho việc quản lý dữ liệu
 * 
 * Implementing class cần override các abstract methods để:
 * - Fetch data từ remote API
 * - Parse API response
 * - Save data vào local database
 * - Load data từ local database
 * 
 * @param LocalType Kiểu dữ liệu trong local database (Entity)
 * @param RemoteType Kiểu dữ liệu từ remote API
 */
abstract class BaseRepository<LocalType, RemoteType> {
    
    /**
     * Fetch data với caching strategy
     * 
     * Pattern: Database → Network → Database
     * 1. Emit dữ liệu từ local database trước
     * 2. Fetch dữ liệu mới từ network
     * 3. Save vào database và emit dữ liệu mới
     * 
     * @param forceRefresh Có force refresh từ network hay không
     * @return Flow<ResultWrapper<LocalType>> với trạng thái loading/success/error
     */
    protected fun networkBoundResource(
        forceRefresh: Boolean = false
    ): Flow<ResultWrapper<List<LocalType>>> = flow {
        
        // Emit loading state
        emit(ResultWrapper.Loading)
        
        // Nếu không force refresh, emit dữ liệu từ local trước
        if (!forceRefresh) {
            val localData = fetchFromLocal().map { ResultWrapper.Success(it) }
            localData.collect { emit(it) }
        }
        
        try {
            // Fetch dữ liệu từ remote
            val remoteData = fetchFromRemote()
            val parsedData = parseRemoteData(remoteData)
            
            // Save vào local database
            saveToLocal(parsedData)
            
            // Emit dữ liệu mới từ local (sau khi đã save)
            val updatedLocalData = fetchFromLocal().map { ResultWrapper.Success(it) }
            updatedLocalData.collect { emit(it) }
            
        } catch (exception: Exception) {
            // Nếu có lỗi, vẫn emit dữ liệu từ local (nếu có)
            val localData = fetchFromLocal()
                .map { 
                    if (it.isNotEmpty()) {
                        ResultWrapper.Success(it)
                    } else {
                        ResultWrapper.Error(
                            message = exception.message ?: "Unknown error",
                            exception = exception
                        )
                    }
                }
                .catch { 
                    emit(ResultWrapper.Error(
                        message = exception.message ?: "Unknown error",
                        exception = exception
                    ))
                }
            
            localData.collect { emit(it) }
        }
    }
    
    /**
     * Safe wrapper cho các operations bất đồng bộ
     * 
     * @param action Action cần thực hiện
     * @return ResultWrapper với kết quả hoặc lỗi
     */
    protected suspend fun <T> safeCall(action: suspend () -> T): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(action())
        } catch (exception: Exception) {
            ResultWrapper.Error(
                message = exception.message ?: "Unknown error",
                exception = exception
            )
        }
    }
    
    /**
     * Fetch dữ liệu từ remote API
     * 
     * @return Raw data từ API
     */
    protected abstract suspend fun fetchFromRemote(): RemoteType
    
    /**
     * Parse dữ liệu từ remote thành local type
     * 
     * @param remoteData Dữ liệu từ API
     * @return Dữ liệu đã parse thành local type
     */
    protected abstract suspend fun parseRemoteData(remoteData: RemoteType): List<LocalType>
    
    /**
     * Save dữ liệu vào local database
     * 
     * @param data Dữ liệu cần save
     */
    protected abstract suspend fun saveToLocal(data: List<LocalType>)
    
    /**
     * Fetch dữ liệu từ local database
     * 
     * @return Flow của dữ liệu local
     */
    protected abstract fun fetchFromLocal(): Flow<List<LocalType>>
} 