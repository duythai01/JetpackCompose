package com.example.jetpackcomposeapp.data.api

import retrofit2.Response
import retrofit2.http.*

/**
 * Interface chung định nghĩa các phương thức HTTP cơ bản để gọi API
 * 
 * Sử dụng @Url để có thể gọi đến bất kỳ endpoint nào một cách linh hoạt
 */
interface BaseApiService {
    
    /**
     * Thực hiện GET request đến một URL bất kỳ
     * 
     * @param url URL đầy đủ hoặc endpoint để gọi
     * @param queries Map các query parameters (tùy chọn)
     * @return Response chứa kết quả dưới dạng String
     */
    @GET
    suspend fun get(
        @Url url: String,
        @QueryMap queries: Map<String, String> = emptyMap()
    ): Response<String>
    
    /**
     * Thực hiện POST request đến một URL bất kỳ
     * 
     * @param url URL đầy đủ hoặc endpoint để gọi
     * @param body Dữ liệu gửi trong request body
     * @param headers Map các headers (tùy chọn)
     * @return Response chứa kết quả dưới dạng String
     */
    @POST
    suspend fun post(
        @Url url: String,
        @Body body: Any,
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Response<String>
    
    /**
     * Thực hiện PUT request đến một URL bất kỳ
     * 
     * @param url URL đầy đủ hoặc endpoint để gọi
     * @param body Dữ liệu gửi trong request body
     * @param headers Map các headers (tùy chọn)
     * @return Response chứa kết quả dưới dạng String
     */
    @PUT
    suspend fun put(
        @Url url: String,
        @Body body: Any,
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Response<String>
    
    /**
     * Thực hiện DELETE request đến một URL bất kỳ
     * 
     * @param url URL đầy đủ hoặc endpoint để gọi
     * @param headers Map các headers (tùy chọn)
     * @return Response chứa kết quả dưới dạng String
     */
    @DELETE
    suspend fun delete(
        @Url url: String,
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Response<String>
} 