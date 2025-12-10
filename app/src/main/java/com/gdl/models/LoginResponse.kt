package com.gdl.models


data class LoginResponse(
    val id: Long,
    val token: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val rut: String
)