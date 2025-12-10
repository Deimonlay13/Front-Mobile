package com.gdl.network

import com.gdl.models.LoginRequest
import com.gdl.models.LoginResponse
import com.gdl.models.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): String
}
