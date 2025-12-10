package com.gdl.repository

import com.gdl.models.RegisterRequest
import com.gdl.network.ApiService

class UserRepository(private val api: ApiService) {

    suspend fun register(
        nombre: String,
        apellido: String,
        email: String,
        contraseña: String,
        rut: String
    ): Result<String> {
        return try {
            val request = RegisterRequest(nombre, apellido, email, contraseña, rut)
            val response = api.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
