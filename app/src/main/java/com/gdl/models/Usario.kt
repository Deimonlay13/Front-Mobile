package com.gdl.models

data class UsuarioEntity(
    val idUsuario: Long? = null,
    val email: String,
    val contraseña: String? = null, // en GET vendrá null, en login sí se usa
    val nombre: String,
    val apellido: String,
    val rut: String,
    val direccion: DireccionEntity? = null
)

