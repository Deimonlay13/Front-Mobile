package com.gdl.models

data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val contraseña: String,
    val rut: String
)


data class RegisterResponse(
    val message: String
)