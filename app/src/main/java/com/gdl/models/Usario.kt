package com.gdl.models

data class UsuarioEntity(
    val id: Long? = null,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val email: String,
    val contraseña: String
)
